package com.rakbow.kureakurusu.service;

import com.rakbow.kureakurusu.controller.interceptor.TokenInterceptor;
import com.rakbow.kureakurusu.dao.CommonMapper;
import com.rakbow.kureakurusu.dao.PersonRoleMapper;
import com.rakbow.kureakurusu.data.*;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.emun.temp.EnumUtil;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.data.meta.MetaOption;
import com.rakbow.kureakurusu.data.entity.PersonRole;
import com.rakbow.kureakurusu.util.EnumHelper;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.*;
import com.rakbow.kureakurusu.util.file.CommonImageUtil;
import com.rakbow.kureakurusu.util.file.QiniuFileUtil;
import com.rakbow.kureakurusu.util.file.QiniuImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.rakbow.kureakurusu.data.common.Constant.*;

/**
 * @author Rakbow
 * @since 2023-05-19 18:56
 */
@RequiredArgsConstructor
@Service
public class GeneralService {

    //region util resource
    private final RedisUtil redisUtil;
    private final VisitUtil visitUtil;
    private final LikeUtil likeUtil;
    private final QiniuFileUtil qiniuFileUtil;
    private final QiniuImageUtil qiniuImageUtil;
    //endregion

    //region mapper
    private final CommonMapper mapper;
    private final PersonRoleMapper personRoleMapper;

    //endregion

    private static final Logger log = LoggerFactory.getLogger(GeneralService.class);

    /**
     * 刷新redis中的选项缓存
     *
     * @author rakbow
     */
    public void refreshRedisEnumData() {

        Map<String, List<Attribute<Integer>>> enumOptionsRedisKeyPair = EnumUtil.getOptionRedisKeyPair();
        enumOptionsRedisKeyPair.forEach(redisUtil::set);

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
        log.info(I18nHelper.getMessage("system.load_data.meta_data"));
    }

    /**
     * 批量更新数据库实体激活状态
     * @param tableName,ids,status 实体表名,ids,状态
     * @author rakbow
     */
    public void updateItemStatus(String tableName, List<Long> ids, int status) {
        mapper.updateItemStatus(tableName, ids, status);
    }

    /**
     * 点赞实体
     * @param entityType,entityId,likeToken 实体表名,实体id,点赞token
     * @author rakbow
     */
    public boolean like(int entityType, long entityId, String likeToken) {
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
    public void updateItemDetail(String tableName, long entityId, String text) {
        mapper.updateItemDetail(tableName, entityId, text, DateHelper.now());
    }

    //endregion

    //region image operation

    /**
     * 根据实体类型和实体Id获取图片
     *
     * @param tableName,entityId 实体表名 实体id
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public segmentImagesResult getItemImages(String tableName, long entityId) {
        //original images
        List<Image> images = JsonUtil.toJavaList(mapper.getItemImages(tableName, entityId), Image.class);
        return CommonImageUtil.segmentImages(images);
    }

    /**
     * 新增图片
     *
     * @param entityType 实体类型
     * @param entityId   实体id
     * @param images     新增图片文件数组
     * @param imageInfos 新增图片json数据
     * @author rakbow
     */
    @SuppressWarnings("unchecked")
    @SneakyThrows
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void addItemImages(int entityType, int entityId, MultipartFile[] images, String imageInfos) {
        String tableName = Entity.getTableName(entityType);
        //原始图片信息json数组
        List<Image> originalImages = JsonUtil.toJavaList(mapper.getItemImages(tableName, entityId), Image.class);
        //新增图片的信息
        List<Image> newImageInfos = JsonUtil.toJavaList(imageInfos, Image.class);
        //检测数据合法性
        CommonImageUtil.checkAddImages(newImageInfos, originalImages);
        //save
        ActionResult ar = qiniuImageUtil.commonAddImages(entityId, tableName, images, originalImages, newImageInfos);
        if(ar.state) {
            mapper.updateItemImages(tableName, entityId, (List<Image>) ar.data, DateHelper.now());
        }else {
            throw new Exception(ar.message);
        }
    }

    /**
     * 更新图片
     *
     * @param entityId     图书id
     * @param images 需要更新的图片json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateItemImages(String tableName, long entityId, List<Image> images) {
        mapper.updateItemImages(tableName, entityId, images, DateHelper.now());
    }

    /**
     * 删除图片
     *
     * @param tableName,entityId,images,deleteImages 实体表名,实体id,原图片信息,删除图片
     * @param deleteImages 需要删除的图片jsonArray
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void deleteItemImages(String tableName, long entityId, List<Image> deleteImages) throws Exception {
        List<Image> images = JsonUtil.toJavaList(mapper.getItemImages(tableName, entityId), Image.class);
        List<Image> finalImageJson = qiniuFileUtil.deleteImage(images, deleteImages);
        mapper.updateItemImages(tableName, entityId, finalImageJson, DateHelper.now());
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
            roleSet.add(new Attribute<>(role.getNameZh() + SLASH + role.getNameEn(), role.getId()));
        });
        return roleSet;
    }
    //endregion

}
