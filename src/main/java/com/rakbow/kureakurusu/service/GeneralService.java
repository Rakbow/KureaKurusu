package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rakbow.kureakurusu.dao.*;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.CommonConstant;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.SimpleSearchParam;
import com.rakbow.kureakurusu.data.dto.UpdateDetailDTO;
import com.rakbow.kureakurusu.data.dto.UpdateStatusDTO;
import com.rakbow.kureakurusu.data.emun.*;
import com.rakbow.kureakurusu.data.entity.Entry;
import com.rakbow.kureakurusu.data.entity.Person;
import com.rakbow.kureakurusu.data.entity.Product;
import com.rakbow.kureakurusu.data.entity.Role;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.data.meta.MetaOption;
import com.rakbow.kureakurusu.data.vo.EntityMiniVO;
import com.rakbow.kureakurusu.toolkit.*;
import com.rakbow.kureakurusu.toolkit.file.CommonImageUtil;
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
    private final ResourceService resourceSrv;
    private final CommonMapper mapper;
    private final RoleMapper roleMapper;
    private final PersonMapper personMapper;
    private final ProductMapper productMapper;
    private final EntryMapper entryMapper;

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

        MetaData.optionsZh.productTypeSet = EnumHelper.getAttributeOptions(ProductType.class, "zh");
        MetaData.optionsEn.productTypeSet = EnumHelper.getAttributeOptions(ProductType.class, "en");

        MetaData.optionsZh.entryTypeSet = EnumHelper.getAttributeOptions(EntryType.class, "zh");
        MetaData.optionsEn.entryTypeSet = EnumHelper.getAttributeOptions(EntryType.class, "en");

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

        MetaData.optionsZh.goodsTypeSet = EnumHelper.getAttributeOptions(GoodsType.class, "zh");
        MetaData.optionsEn.goodsTypeSet = EnumHelper.getAttributeOptions(GoodsType.class, "en");

        MetaData.optionsZh.figureTypeSet = EnumHelper.getAttributeOptions(FigureType.class, "zh");
        MetaData.optionsEn.figureTypeSet = EnumHelper.getAttributeOptions(FigureType.class, "en");

        MetaData.optionsZh.relatedGroupSet = EnumHelper.getAttributeOptions(RelatedGroup.class, "zh");
        MetaData.optionsEn.relatedGroupSet = EnumHelper.getAttributeOptions(RelatedGroup.class, "en");

        MetaData.optionsZh.roleSet = getPersonRoleSet();
        MetaData.optionsZh.roleSet.sort(DataSorter.attributesLongValueSorter);
        MetaData.optionsEn.roleSet = MetaData.optionsZh.roleSet;

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
     *
     * @param entityType,entityId,likeToken 实体表名,实体id,点赞token
     * @author rakbow
     */
    @Transactional
    public boolean like(int entityType, long entityId, String likeToken) {
        //点过赞
        if (likeUtil.isLike(entityType, entityId, likeToken)) {
            return false;
        } else {//没点过赞,自增
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
    public void refreshRoleSet() {
        MetaData.optionsZh.roleSet.clear();
        MetaData.optionsEn.roleSet.clear();
        MetaData.optionsZh.roleSet = getPersonRoleSet();
        MetaData.optionsZh.roleSet.sort(DataSorter.attributesLongValueSorter);
        MetaData.optionsEn.roleSet = MetaData.optionsZh.roleSet;
    }

    private List<Attribute<Long>> getPersonRoleSet() {
        List<Attribute<Long>> res = new ArrayList<>();
        //获取所有role数据
        List<Role> items = roleMapper.selectList(null);
        items.forEach(i -> res.add(new Attribute<>(i.getNameZh() + SLASH + i.getNameEn(), i.getId())));
        return res;
    }
    //endregion

    public SearchResult<EntityMiniVO> search(int entityType, SimpleSearchParam param) {
        List<EntityMiniVO> res = new ArrayList<>();
        if (entityType == EntityType.PERSON.getValue()) {
            LambdaQueryWrapper<Person> wrapper = new LambdaQueryWrapper<Person>()
                    .eq(Person::getStatus, 1)
                    .orderByDesc(Person::getId);
            if (!param.keywordEmpty()) {
                wrapper.and(i -> i.apply("JSON_UNQUOTE(JSON_EXTRACT(aliases, '$[*]')) LIKE concat('%', {0}, '%')", param.getKeyword()))
                        .or().like(Person::getName, param.getKeyword())
                        .or().like(Person::getNameZh, param.getKeyword())
                        .or().like(Person::getNameEn, param.getKeyword());
            }

            IPage<Person> pages = personMapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);
            pages.getRecords()
                    .forEach(i -> res.add(new EntityMiniVO(
                            CommonImageUtil.getEntryThumbCover(i.getImage()),
                            entityType,
                            i.getId(),
                            i.getName(),
                            STR."\{i.getNameEn()}/\{i.getNameZh()}",
                            ""
                    )));
            return new SearchResult<>(res, pages.getTotal(), pages.getCurrent(), pages.getSize());
        } else if (entityType == EntityType.PRODUCT.getValue()) {
            LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<Product>()
                    .eq(Product::getStatus, 1)
                    .orderByDesc(Product::getId);
            if (!param.keywordEmpty()) {
                wrapper.and(i -> i.apply("JSON_UNQUOTE(JSON_EXTRACT(aliases, '$[*]')) LIKE concat('%', {0}, '%')", param.getKeyword()))
                        .or().like(Product::getName, param.getKeyword())
                        .or().like(Product::getNameZh, param.getKeyword())
                        .or().like(Product::getNameEn, param.getKeyword());
            }

            IPage<Product> pages = productMapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);
            pages.getRecords()
                    .forEach(i -> res.add(new EntityMiniVO(
                            resourceSrv.getEntityCover(EntityType.PRODUCT, i.getId()),
                            entityType,
                            i.getId(),
                            i.getName(),
                            STR."\{i.getNameEn()}/\{i.getNameZh()}",
                            I18nHelper.getMessage(i.getType().getLabelKey())
                    )));
            return new SearchResult<>(res, pages.getTotal(), pages.getCurrent(), pages.getSize());
        } else if (entityType == EntityType.ENTRY.getValue()) {
            LambdaQueryWrapper<Entry> wrapper = new LambdaQueryWrapper<Entry>()
                    .eq(Entry::getStatus, 1)
                    .orderByDesc(Entry::getId);
            if (!param.keywordEmpty()) {
                wrapper.and(i -> i.apply("JSON_UNQUOTE(JSON_EXTRACT(aliases, '$[*]')) LIKE concat('%', {0}, '%')", param.getKeyword()))
                        .or().like(Entry::getName, param.getKeyword())
                        .or().like(Entry::getNameEn, param.getKeyword());
            }

            IPage<Entry> pages = entryMapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);
            pages.getRecords()
                    .forEach(i -> res.add(new EntityMiniVO(
                            CommonImageUtil.getEntryThumbCover(i.getImage()),
                            entityType,
                            i.getId(),
                            i.getName(),
                            i.getNameEn(),
                            I18nHelper.getMessage(i.getType().getLabelKey())
                    )));
            return new SearchResult<>(res, pages.getTotal(), pages.getCurrent(), pages.getSize());
        }

        return null;
    }

}
