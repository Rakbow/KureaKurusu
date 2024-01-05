package com.rakbow.kureakurusu.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.rakbow.kureakurusu.controller.interceptor.TokenInterceptor;
import com.rakbow.kureakurusu.dao.CommonMapper;
import com.rakbow.kureakurusu.dao.PersonRoleMapper;
import com.rakbow.kureakurusu.data.*;
import com.rakbow.kureakurusu.data.emun.temp.EnumUtil;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.data.meta.MetaOption;
import com.rakbow.kureakurusu.entity.PersonRole;
import com.rakbow.kureakurusu.util.EnumHelper;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.common.LikeUtil;
import com.rakbow.kureakurusu.util.common.RedisUtil;
import com.rakbow.kureakurusu.util.common.VisitUtil;
import com.rakbow.kureakurusu.util.file.QiniuFileUtil;
import com.rakbow.kureakurusu.util.file.QiniuImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Rakbow
 * @since 2023-05-19 18:56
 */
@Service
public class GeneralService {

    //region util resource
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private VisitUtil visitUtil;
    @Resource
    private LikeUtil likeUtil;
    @Resource
    private QiniuFileUtil qiniuFileUtil;
    @Resource
    private QiniuImageUtil qiniuImageUtil;
    //endregion

    //region mapper
    @Resource
    private CommonMapper commonMapper;
    @Resource
    private PersonRoleMapper personRoleMapper;

    //endregion

    private static final Logger logger = LoggerFactory.getLogger(GeneralService.class);

    /**
     * 刷新redis中的选项缓存
     *
     * @author rakbow
     */
    public void refreshRedisEnumData() {

        Map<String, List<Attribute<Integer>>> enumOptionsRedisKeyPair = EnumUtil.getOptionRedisKeyPair();
        enumOptionsRedisKeyPair.forEach((k, v) -> redisUtil.set(k, v));

    }

    //region common

    public void loadMetaData() {

        MetaData.optionsZh = new MetaOption();
        MetaData.optionsEn = new MetaOption();
        MetaData.optionsZh.genderSet = EnumHelper.getAttributeOptions(Gender.class, "zh");
        MetaData.optionsEn.genderSet = EnumHelper.getAttributeOptions(Gender.class, "en");
        MetaData.optionsZh.linkTypeSet = EnumHelper.getAttributeOptions(LinkType.class, "zh");
        MetaData.optionsEn.linkTypeSet = EnumHelper.getAttributeOptions(LinkType.class, "en");
        MetaData.optionsZh.roleSet = getPersonRoleSet();
        MetaData.optionsEn.roleSet = MetaData.optionsZh.roleSet;

        logger.info(I18nHelper.getMessage("system.load_data.meta_data"));
    }

    /**
     * 获取页面数据
     *
     * @param entityType,entityId 实体类型，实体id
     * @author Rakbow
     */
    public PageTraffic getPageTraffic(int entityType, long entityId) {

        PageTraffic pt = new PageTraffic();

        // 从cookie中获取点赞token和访问token
        String likeToken = TokenInterceptor.getLikeToken();
        String visitToken = TokenInterceptor.getVisitToken();

        pt.setLiked(likeUtil.isLike(entityType, entityId, likeToken));
        pt.setLikeCount(likeUtil.getLike(entityType, entityId));
        pt.setVisitCount(visitUtil.incVisit(entityType, entityId, visitToken));

        return pt;
    }

    /**
     * 批量更新数据库实体激活状态
     * @param tableName,ids,status 实体表名,ids,状态
     * @author rakbow
     */
    public void updateItemStatus(String tableName, List<Integer> ids, int status) {
        commonMapper.updateItemStatus(tableName, ids, status);
    }

    /**
     * 点赞实体
     * @param entityType,entityId,likeToken 实体表名,实体id,点赞token
     * @author rakbow
     */
    public boolean entityLike(int entityType, int entityId, String likeToken) {
        //点过赞
        if(likeUtil.isLike(entityType, entityId, likeToken)) {
            return false;
        }else {//没点过赞,自增
            likeUtil.incLike(entityType, entityId, likeToken);
            return true;
        }
    }

    /**
     * 更新描述
     *
     * @param tableName,id 实体表名,实体id
     * @param text 描述json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateItemDetail(String tableName, int id, String text) {
        commonMapper.updateItemDetail(tableName, id, text, DateHelper.now());
    }

    //endregion

    //region image operation

    /**
     * 根据实体类型和实体Id获取图片
     *
     * @param tableName,entityId 实体表名 实体id
     * @return JSONArray
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<Image> getItemImages(String tableName, long entityId) {
        return commonMapper.getItemImages(tableName, entityId);
    }

    /**
     * 新增图片
     *
     * @param entityId           实体id
     * @param images             新增图片文件数组
     * @param originalImagesJson 数据库中现存的图片json数据
     * @param newImageInfos         新增图片json数据
     * @author rakbow
     */
    @SuppressWarnings("unchecked")
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public ActionResult addItemImages(String tableName, int entityId, MultipartFile[] images, List<Image> originalImagesJson,
                                      List<Image> newImageInfos) {
        ActionResult res = new ActionResult();
        try{
            ActionResult ar = qiniuImageUtil.commonAddImages(entityId, tableName, images, originalImagesJson, newImageInfos);
            if(ar.state) {
                commonMapper.updateItemImages(tableName, entityId, (List<Image>) ar.data, DateHelper.now());
                res.message = I18nHelper.getMessage("image.insert.success");
            }else {
                throw new Exception(ar.message);
            }
        }catch(Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return res;
    }

    /**
     * 更新图片
     *
     * @param entityId     图书id
     * @param images 需要更新的图片json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String updateItemImages(String tableName, long entityId, List<Image> images) {
        commonMapper.updateItemImages(tableName, entityId, images, DateHelper.now());
        return I18nHelper.getMessage("image.update.success");
    }

    /**
     * 删除图片
     *
     * @param tableName,entityId,images,deleteImages 实体表名,实体id,原图片信息,删除图片
     * @param deleteImages 需要删除的图片jsonArray
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String deleteItemImages(String tableName, long entityId, List<Image> deleteImages) throws Exception {

        List<Image> images = getItemImages(tableName, entityId);

        List<Image> finalImageJson = qiniuFileUtil.deleteImage(images, deleteImages);

        commonMapper.updateItemImages(tableName, entityId, finalImageJson, DateHelper.now());
        return I18nHelper.getMessage("image.delete.success");
    }

    //endregion

    //region person role
    public void refreshPersonRoleSet() {
        MetaData.optionsZh.roleSet.clear();
        MetaData.optionsEn.roleSet.clear();
        MetaData.optionsZh.roleSet = getPersonRoleSet();
        MetaData.optionsEn.roleSet = MetaData.optionsZh.roleSet;
    }

    private List<Attribute<Long>> getPersonRoleSet() {
        List<Attribute<Long>> roleSet = new ArrayList<>();
        //获取所有role数据
        List<PersonRole> allRoleSet = personRoleMapper.selectList(null);
        allRoleSet.forEach(role -> {
            roleSet.add(new Attribute<>(role.getNameZh(), role.getId()));
        });
        return roleSet;
    }
    //endregion

}
