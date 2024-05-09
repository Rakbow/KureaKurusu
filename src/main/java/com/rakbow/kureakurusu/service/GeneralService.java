package com.rakbow.kureakurusu.service;

import com.rakbow.kureakurusu.dao.*;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.emun.Gender;
import com.rakbow.kureakurusu.data.emun.LinkType;
import com.rakbow.kureakurusu.data.dto.UpdateDetailDTO;
import com.rakbow.kureakurusu.data.dto.UpdateStatusDTO;
import com.rakbow.kureakurusu.data.emun.*;
import com.rakbow.kureakurusu.data.entity.Franchise;
import com.rakbow.kureakurusu.data.entity.PersonRole;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.data.meta.MetaOption;
import com.rakbow.kureakurusu.data.segmentImagesResult;
import com.rakbow.kureakurusu.data.common.ActionResult;
import com.rakbow.kureakurusu.toolkit.EnumHelper;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import com.rakbow.kureakurusu.toolkit.DataSorter;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.JsonUtil;
import com.rakbow.kureakurusu.toolkit.LikeUtil;
import com.rakbow.kureakurusu.toolkit.file.CommonImageUtil;
import com.rakbow.kureakurusu.toolkit.file.QiniuImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.rakbow.kureakurusu.data.common.Constant.SLASH;

/**
 * @author Rakbow
 * @since 2023-05-19 18:56
 */
@RequiredArgsConstructor
@Service
public class GeneralService {

    //region util resource
    private final LikeUtil likeUtil;
    private final QiniuImageUtil qiniuImageUtil;
    //endregion

    //region mapper
    private final CommonMapper mapper;
    private final PersonRoleMapper personRoleMapper;
    private final FranchiseMapper franchiseMapper;

    //endregion

    private static final Logger log = LoggerFactory.getLogger(GeneralService.class);

    /**
     * 刷新redis中的选项缓存
     *
     * @author rakbow
     */
    // public void refreshRedisEnumData() {
    //
    //     Map<String, List<Attribute<Integer>>> enumOptionsRedisKeyPair = EnumUtil.getOptionRedisKeyPair();
    //     enumOptionsRedisKeyPair.forEach(redisUtil::set);
    //
    // }

    //region common

    @Transactional
    public void loadMetaData() {
        MetaData.optionsZh = new MetaOption();
        MetaData.optionsEn = new MetaOption();

        MetaData.optionsZh.genderSet = EnumHelper.getAttributeOptions(Gender.class, "zh");
        MetaData.optionsEn.genderSet = EnumHelper.getAttributeOptions(Gender.class, "en");

        MetaData.optionsZh.linkTypeSet = EnumHelper.getAttributeOptions(LinkType.class, "zh");
        MetaData.optionsEn.linkTypeSet = EnumHelper.getAttributeOptions(LinkType.class, "en");

        MetaData.optionsZh.albumFormatSet = EnumHelper.getAttributeOptions(AlbumFormat.class, "zh");
        MetaData.optionsEn.albumFormatSet = EnumHelper.getAttributeOptions(AlbumFormat.class, "en");

        MetaData.optionsZh.publishFormatSet = EnumHelper.getAttributeOptions(PublishFormat.class, "zh");
        MetaData.optionsEn.publishFormatSet = EnumHelper.getAttributeOptions(PublishFormat.class, "en");

        MetaData.optionsZh.mediaFormatSet = EnumHelper.getAttributeOptions(MediaFormat.class, "zh");
        MetaData.optionsEn.mediaFormatSet = EnumHelper.getAttributeOptions(MediaFormat.class, "en");

        MetaData.optionsZh.productCategorySet = EnumHelper.getAttributeOptions(ProductCategory.class, "zh");
        MetaData.optionsEn.productCategorySet = EnumHelper.getAttributeOptions(ProductCategory.class, "en");

        MetaData.optionsZh.relationTypeSet = EnumHelper.getAttributeOptions(RelatedType.class, "zh");
        MetaData.optionsEn.relationTypeSet = EnumHelper.getAttributeOptions(RelatedType.class, "en");

        MetaData.optionsZh.entityTypeSet = EnumHelper.getAttributeOptions(EntityType.class, "zh");
        MetaData.optionsEn.entityTypeSet = EnumHelper.getAttributeOptions(EntityType.class, "en");

        MetaData.optionsZh.bookTypeSet = EnumHelper.getAttributeOptions(BookType.class, "zh");
        MetaData.optionsEn.bookTypeSet = EnumHelper.getAttributeOptions(BookType.class, "en");

        MetaData.optionsZh.regionSet = EnumHelper.getAttributeStrOptions(Region.class, "zh");
        MetaData.optionsEn.regionSet = EnumHelper.getAttributeStrOptions(Region.class, "en");

        MetaData.optionsZh.languageSet = EnumHelper.getAttributeStrOptions(Language.class, "zh");
        MetaData.optionsEn.languageSet = EnumHelper.getAttributeStrOptions(Language.class, "en");

        MetaData.optionsZh.currencySet = EnumHelper.getAttributeStrOptions(Currency.class, "zh");
        MetaData.optionsEn.currencySet = EnumHelper.getAttributeStrOptions(Currency.class, "en");

        MetaData.optionsZh.roleSet = getPersonRoleSet();
        MetaData.optionsZh.roleSet.sort(DataSorter.attributesLongValueSorter);
        MetaData.optionsEn.roleSet = MetaData.optionsZh.roleSet;

        MetaData.optionsZh.franchiseSet = getFranchiseSet();
        MetaData.optionsEn.franchiseSet = MetaData.optionsZh.franchiseSet;

        log.info(I18nHelper.getMessage("system.load_data.meta_data"));
    }

    /**
     * 批量更新数据库实体激活状态
     *
     * @author rakbow
     */
    @Transactional
    public void updateEntryStatus(UpdateStatusDTO dto) {
        mapper.updateEntryStatus(EntityType.getTableName(dto.getEntity()), dto.getIds(), dto.status());
    }

    /**
     * 点赞实体
     * @param entityType,entityId,likeToken 实体表名,实体id,点赞token
     * @author rakbow
     */
    @Transactional
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
     * @author rakbow
     */
    @Transactional
    public void updateEntryDetail(UpdateDetailDTO dto) {
        mapper.updateEntryDetail(EntityType.getTableName(dto.getEntityType()), dto.getEntityId(), dto.getText(), DateHelper.now());
    }

    //endregion

    //region image operation

    /**
     * 根据实体类型和实体Id获取图片
     *
     * @param entityType,entityId 实体类型 实体id
     * @author rakbow
     */
    @Transactional
    public segmentImagesResult getEntryImages(int entityType, long entityId) {
        String tableName = EntityType.getTableName(entityType);
        //original images
        String imageJson = mapper.getEntryImages(tableName, entityId);
        List<Image> images = JsonUtil.toJavaList(imageJson, Image.class);
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
    @Transactional
    public void addEntryImages(int entityType, int entityId, MultipartFile[] images, String imageInfos) {
        String tableName = EntityType.getTableName(entityType);
        //原始图片信息json数组
        List<Image> originalImages = JsonUtil.toJavaList(mapper.getEntryImages(tableName, entityId), Image.class);
        //新增图片的信息
        List<Image> newImageInfos = JsonUtil.toJavaList(imageInfos, Image.class);
        //检测数据合法性
        CommonImageUtil.checkAddImages(newImageInfos, originalImages);
        //save
        ActionResult ar = qiniuImageUtil.commonAddImages(entityId, tableName, images, originalImages, newImageInfos);
        if(ar.state) {
            mapper.updateEntryImages(tableName, entityId, (List<Image>) ar.data, DateHelper.now());
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
    @Transactional
    public void updateEntryImages(int entityType, long entityId, List<Image> images) {
        mapper.updateEntryImages(EntityType.getTableName(entityType), entityId, images, DateHelper.now());
    }

    /**
     * 删除图片
     *
     * @param entityType,entityId,images,deleteImages 实体类型,实体id,原图片信息,删除图片
     * @param deleteImages 需要删除的图片jsonArray
     * @author rakbow
     */
    @Transactional
    public void deleteEntryImages(int entityType, long entityId, List<Image> deleteImages) {
        String tableName = EntityType.getTableName(entityType);
        List<Image> images = JsonUtil.toJavaList(mapper.getEntryImages(tableName, entityId), Image.class);
        List<Image> finalImageJson = qiniuImageUtil.deleteImage(images, deleteImages);
        mapper.updateEntryImages(tableName, entityId, finalImageJson, DateHelper.now());
    }

    //endregion

    //region person role
    @Transactional
    public void refreshPersonRoleSet() {
        MetaData.optionsZh.roleSet.clear();
        MetaData.optionsEn.roleSet.clear();
        MetaData.optionsZh.roleSet = getPersonRoleSet();
        MetaData.optionsEn.roleSet = MetaData.optionsZh.roleSet;
    }

    private List<Attribute<Long>> getPersonRoleSet() {
        List<Attribute<Long>> res = new ArrayList<>();
        //获取所有role数据
        List<PersonRole> items = personRoleMapper.selectList(null);
        items.forEach(i -> res.add(new Attribute<>(i.getNameZh() + SLASH + i.getNameEn(), i.getId())));
        return res;
    }

    private List<Attribute<Long>> getFranchiseSet() {
        List<Attribute<Long>> res = new ArrayList<>();
        //获取所有role数据
        List<Franchise> items = franchiseMapper.selectList(null);
        items.sort(DataSorter.franchiseIdSorter);
        items.forEach(i -> res.add(new Attribute<>(i.getName(), i.getId())));
        return res;
    }
    //endregion

}
