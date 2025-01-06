package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rakbow.kureakurusu.dao.*;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.SimpleSearchParam;
import com.rakbow.kureakurusu.data.dto.UpdateDetailDTO;
import com.rakbow.kureakurusu.data.dto.UpdateStatusDTO;
import com.rakbow.kureakurusu.data.emun.*;
import com.rakbow.kureakurusu.data.entity.*;
import com.rakbow.kureakurusu.data.entity.entry.Entry;
import com.rakbow.kureakurusu.data.entity.entry.Chara;
import com.rakbow.kureakurusu.data.entity.entry.Person;
import com.rakbow.kureakurusu.data.entity.entry.Product;
import com.rakbow.kureakurusu.data.entity.entry.Subject;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.data.meta.MetaOption;
import com.rakbow.kureakurusu.data.vo.EntityMiniVO;
import com.rakbow.kureakurusu.toolkit.*;
import com.rakbow.kureakurusu.toolkit.file.CommonImageUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    private final SubjectMapper subjectMapper;
    private final CharaMapper charMapper;

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

        MetaData.optionsZh.subjectTypeSet = EnumHelper.getAttributeOptions(SubjectType.class, "zh");
        MetaData.optionsEn.subjectTypeSet = EnumHelper.getAttributeOptions(SubjectType.class, "en");

        MetaData.optionsZh.entityTypeSet = EnumHelper.getAttributeOptions(EntityType.class, "zh");
        MetaData.optionsEn.entityTypeSet = EnumHelper.getAttributeOptions(EntityType.class, "en");

        MetaData.optionsZh.releaseTypeSet = EnumHelper.getAttributeOptions(ReleaseType.class, "zh");
        MetaData.optionsEn.releaseTypeSet = EnumHelper.getAttributeOptions(ReleaseType.class, "en");

        MetaData.optionsZh.languageSet = EnumHelper.getAttributeStrOptions(Language.class, "zh");
        MetaData.optionsEn.languageSet = EnumHelper.getAttributeStrOptions(Language.class, "en");

        MetaData.optionsZh.bookTypeSet = ItemSubType.getItemSubTypeSet(ItemType.BOOK.getValue(), "zh");
        MetaData.optionsEn.bookTypeSet = ItemSubType.getItemSubTypeSet(ItemType.BOOK.getValue(), "en");

        MetaData.optionsZh.goodsTypeSet = ItemSubType.getItemSubTypeSet(ItemType.GOODS.getValue(), "zh");
        MetaData.optionsEn.goodsTypeSet = ItemSubType.getItemSubTypeSet(ItemType.GOODS.getValue(), "en");

        MetaData.optionsZh.figureTypeSet = ItemSubType.getItemSubTypeSet(ItemType.FIGURE.getValue(), "zh");
        MetaData.optionsEn.figureTypeSet = ItemSubType.getItemSubTypeSet(ItemType.FIGURE.getValue(), "en");

        MetaData.optionsZh.relatedGroupSet = EnumHelper.getAttributeOptions(RelatedGroup.class, "zh");
        MetaData.optionsEn.relatedGroupSet = EnumHelper.getAttributeOptions(RelatedGroup.class, "en");

        MetaData.optionsZh.imageTypeSet = EnumHelper.getAttributeOptions(ImageType.class, "zh");
        MetaData.optionsEn.imageTypeSet = EnumHelper.getAttributeOptions(ImageType.class, "en");

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

    public SearchResult<EntityMiniVO> search(int entrySearchType, SimpleSearchParam param) {
        List<EntityMiniVO> res = new ArrayList<>();
        IPage<? extends Entry> pages;
        if (entrySearchType == EntrySearchType.PRODUCT.getValue()) {
            LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<Product>()
                    .eq(Product::getStatus, 1)
                    .orderByAsc(Product::getId);
            if (!param.keywordEmpty()) {
                wrapper.and(i -> i.apply("JSON_UNQUOTE(JSON_EXTRACT(aliases, '$[*]')) LIKE concat('%', {0}, '%')", param.getKeyword()))
                        .or().like(Product::getName, param.getKeyword())
                        .or().like(Product::getNameZh, param.getKeyword())
                        .or().like(Product::getNameEn, param.getKeyword());
            }

            pages = productMapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);
            pages.getRecords()
                    .stream()
                    .map(Product.class::cast)
                    .forEach(i -> res.add(new EntityMiniVO(
                            resourceSrv.getEntityCover(EntityType.PRODUCT, i.getId()),
                            EntityType.PRODUCT.getValue(),
                            i.getId(),
                            i.getName(),
                            STR."\{i.getNameEn()}/\{i.getNameZh()}",
                            I18nHelper.getMessage(i.getType().getLabelKey())
                    )));
        } else if (entrySearchType == EntrySearchType.PERSON.getValue()) {
            LambdaQueryWrapper<Person> wrapper = new LambdaQueryWrapper<Person>()
                    .eq(Person::getStatus, 1)
                    .orderByAsc(Person::getId);
            if (!param.keywordEmpty()) {
                wrapper.and(i -> i.apply("JSON_UNQUOTE(JSON_EXTRACT(aliases, '$[*]')) LIKE concat('%', {0}, '%')", param.getKeyword()))
                        .or().like(Person::getName, param.getKeyword())
                        .or().like(Person::getNameZh, param.getKeyword())
                        .or().like(Person::getNameEn, param.getKeyword());
            }

            pages = personMapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);
            pages.getRecords()
                    .stream()
                    .map(Person.class::cast)
                    .forEach(i -> res.add(new EntityMiniVO(
                            CommonImageUtil.getEntryThumb(i.getThumb()),
                            EntityType.PERSON.getValue(),
                            i.getId(),
                            i.getName(),
                            STR."\{i.getNameEn()}/\{i.getNameZh()}",
                            ""
                    )));
        } else if (entrySearchType == EntrySearchType.CHARACTER.getValue()) {
            LambdaQueryWrapper<Chara> wrapper = new LambdaQueryWrapper<Chara>()
                    .eq(Chara::getStatus, 1)
                    .orderByAsc(Chara::getId);
            if (!param.keywordEmpty()) {
                wrapper.and(i -> i.apply("JSON_UNQUOTE(JSON_EXTRACT(aliases, '$[*]')) LIKE concat('%', {0}, '%')", param.getKeyword()))
                        .or().like(Chara::getName, param.getKeyword())
                        .or().like(Chara::getNameZh, param.getKeyword())
                        .or().like(Chara::getNameEn, param.getKeyword());
            }
            pages = charMapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);
            pages.getRecords()
                    .forEach(i -> res.add(new EntityMiniVO(
                            CommonImageUtil.getEntryThumb(i.getThumb()),
                            EntityType.CHARACTER.getValue(),
                            i.getId(),
                            i.getName(),
                            STR."\{i.getNameZh()} / \{i.getNameEn()}",
                            StringUtils.EMPTY
                    )));
        } else {
            LambdaQueryWrapper<Subject> wrapper = new LambdaQueryWrapper<Subject>()
                    .eq(Subject::getStatus, 1)
                    .orderByAsc(Subject::getId);

            if (entrySearchType == EntrySearchType.CLASSIFICATION.getValue()) {
                wrapper.eq(Subject::getType, SubjectType.CLASSIFICATION);
            } else if (entrySearchType == EntrySearchType.MATERIAL.getValue()) {
                wrapper.eq(Subject::getType, SubjectType.MATERIAL);
            } else if (entrySearchType == EntrySearchType.EVENT.getValue()) {
                wrapper.eq(Subject::getType, SubjectType.EVENT);
            }
            if (!param.keywordEmpty()) {
                wrapper.and(i -> i.apply("JSON_UNQUOTE(JSON_EXTRACT(aliases, '$[*]')) LIKE concat('%', {0}, '%')", param.getKeyword()))
                        .or().like(Subject::getName, param.getKeyword())
                        .or().like(Subject::getNameEn, param.getKeyword());
            }

            pages = subjectMapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);
            pages.getRecords()
                    .stream()
                    .map(Subject.class::cast)
                    .forEach(i -> res.add(new EntityMiniVO(
                            CommonImageUtil.getEntryThumb(i.getThumb()),
                            EntityType.SUBJECT.getValue(),
                            i.getId(),
                            i.getName(),
                            i.getNameEn(),
                            StringUtils.EMPTY
                    )));
        }

        return new SearchResult<>(res, pages.getTotal(), pages.getCurrent(), pages.getSize());
    }

    public Map<String, Object> getOptions() {
        Map<String, Object> res = new HashMap<>();
        res.put("albumFormatSet", Objects.requireNonNull(MetaData.getOptions()).albumFormatSet);
        res.put("mediaFormatSet", Objects.requireNonNull(MetaData.getOptions()).mediaFormatSet);
        res.put("languageSet", Objects.requireNonNull(MetaData.getOptions()).languageSet);
        res.put("bookTypeSet", Objects.requireNonNull(MetaData.getOptions()).bookTypeSet);
        res.put("goodsTypeSet", Objects.requireNonNull(MetaData.getOptions()).goodsTypeSet);
        res.put("figureTypeSet", Objects.requireNonNull(MetaData.getOptions()).figureTypeSet);
        res.put("releaseTypeSet", Objects.requireNonNull(MetaData.getOptions()).releaseTypeSet);
        res.put("relatedGroupSet", Objects.requireNonNull(MetaData.getOptions()).relatedGroupSet);
        res.put("productTypeSet", Objects.requireNonNull(MetaData.getOptions()).productTypeSet);
        res.put("genderSet", Objects.requireNonNull(MetaData.getOptions()).genderSet);
        res.put("linkTypeSet", Objects.requireNonNull(MetaData.getOptions()).linkTypeSet);
        res.put("roleSet", Objects.requireNonNull(MetaData.getOptions()).roleSet);
        res.put("entityTypeSet", Objects.requireNonNull(MetaData.getOptions()).entityTypeSet);
        res.put("imageTypeSet", Objects.requireNonNull(MetaData.getOptions()).imageTypeSet);
        res.put("subjectTypeSet", Objects.requireNonNull(MetaData.getOptions()).subjectTypeSet);
        return res;
    }

}
