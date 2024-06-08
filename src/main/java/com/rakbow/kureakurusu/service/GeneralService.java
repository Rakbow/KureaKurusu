package com.rakbow.kureakurusu.service;

import com.rakbow.kureakurusu.dao.CommonMapper;
import com.rakbow.kureakurusu.dao.FranchiseMapper;
import com.rakbow.kureakurusu.dao.PersonRoleMapper;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.dto.UpdateDetailDTO;
import com.rakbow.kureakurusu.data.dto.UpdateStatusDTO;
import com.rakbow.kureakurusu.data.emun.*;
import com.rakbow.kureakurusu.data.entity.Franchise;
import com.rakbow.kureakurusu.data.entity.PersonRole;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.data.meta.MetaOption;
import com.rakbow.kureakurusu.toolkit.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        MetaData.optionsZh.releaseTypeSet = EnumHelper.getAttributeOptions(ReleaseType.class, "zh");
        MetaData.optionsEn.releaseTypeSet = EnumHelper.getAttributeOptions(ReleaseType.class, "en");

        MetaData.optionsZh.languageSet = EnumHelper.getAttributeStrOptions(Language.class, "zh");
        MetaData.optionsEn.languageSet = EnumHelper.getAttributeStrOptions(Language.class, "en");

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
            likeUtil.inc(entityType, entityId, likeToken);
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
