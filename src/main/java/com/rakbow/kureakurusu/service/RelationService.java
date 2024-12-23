package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.rakbow.kureakurusu.dao.EntryMapper;
import com.rakbow.kureakurusu.dao.ItemMapper;
import com.rakbow.kureakurusu.dao.RelationMapper;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.RelationCreateDTO;
import com.rakbow.kureakurusu.data.dto.RelationListParams;
import com.rakbow.kureakurusu.data.dto.RelationUpdateDTO;
import com.rakbow.kureakurusu.data.emun.*;
import com.rakbow.kureakurusu.data.entity.Entry;
import com.rakbow.kureakurusu.data.entity.Item;
import com.rakbow.kureakurusu.data.entity.Product;
import com.rakbow.kureakurusu.data.entity.Relation;
import com.rakbow.kureakurusu.data.entity.common.MetaEntity;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.data.result.ItemExcRelatedEntries;
import com.rakbow.kureakurusu.data.vo.item.ItemMiniVO;
import com.rakbow.kureakurusu.data.vo.relation.RelationVO;
import com.rakbow.kureakurusu.toolkit.*;
import com.rakbow.kureakurusu.toolkit.file.CommonImageUtil;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Rakbow
 * @since 2024/2/28 15:19
 */
@Service
@RequiredArgsConstructor
public class RelationService extends ServiceImpl<RelationMapper, Relation> {

    private final RelationMapper mapper;
    private final SqlSessionFactory sqlSessionFactory;
    private final EntityUtil entityUtil;
    private final ResourceService resourceSrv;
    private final EntryMapper entryMapper;
    private final ItemMapper itemMapper;
    private final Converter converter;

    public List<RelationVO> getRelatedEntity(int direction, int relatedGroup, int entityType, long entityId) {
        int relatedEntity;
        String remark;
        List<RelationVO> res = new ArrayList<>();
        if (MetaData.optionsZh.roleSet.isEmpty())
            return res;
        List<Relation> relations = mapper.selectList(
                new LambdaQueryWrapper<Relation>()
                        .eq(Relation::getRelatedGroup, relatedGroup)
                        .eq(direction == 1 ? Relation::getEntityType : Relation::getRelatedEntityType, entityType)
                        .eq(direction == 1 ? Relation::getEntityId : Relation::getRelatedEntityId, entityId)
                        .orderByAsc(direction == 1 ? Relation::getRoleId : Relation::getReverseRoleId)
        );
        if (relations.isEmpty())
            return res;
        if (relatedGroup == RelatedGroup.RELATED_PERSON.getValue()) {
            relatedEntity = EntityType.PERSON.getValue();
        } else if (relatedGroup == RelatedGroup.RELATED_PRODUCT.getValue()) {
            relatedEntity = EntityType.PRODUCT.getValue();
        } else {
            relatedEntity = EntityType.ITEM.getValue();
        }
        List<Long> targetIds = relations.stream().map(direction == 1 ? Relation::getRelatedEntityId : Relation::getEntityId).distinct().toList();
        Class<? extends MetaEntity> subClass = entityUtil.getSubEntity(relatedEntity);
        BaseMapper<MetaEntity> subMapper = MyBatisUtil.getMapper(subClass);
        List<MetaEntity> targets = subMapper.selectByIds(targetIds);
        for (Relation r : relations) {
            MetaEntity entity = DataFinder.findEntityById(direction == 1 ? r.getRelatedEntityId() : r.getEntityId(), targets);
            if (entity == null) continue;
            if (entity instanceof Product) {
                remark = I18nHelper.getMessage(((Product) entity).getType().getLabelKey());
            } else {
                remark = r.getRemark();
            }
            Attribute<Long> role = DataFinder.findAttributeByValue(direction == 1 ? r.getRoleId() : r.getReverseRoleId(), MetaData.optionsZh.roleSet);
            if (role == null) continue;
            res.add(
                    RelationVO.builder()
                            .id(r.getId())
                            .role(role)
                            .target(new Attribute<>(entity.getName(), entity.getId()))
                            .remark(remark)
                            .relatedTypeName(EntityType.getTableName(relatedEntity))
                            .cover(resourceSrv.getThumbCover(relatedEntity, direction == 1 ? r.getRelatedEntityId() : r.getEntityId()))
                            .build()
            );
        }
        return res;
    }

    public SearchResult<RelationVO> getRelations(RelationListParams param) {
        List<RelationVO> res = new ArrayList<>();
        List<Long> targetIds;
        Class<? extends MetaEntity> subClass;
        BaseMapper<MetaEntity> subMapper;
        List<MetaEntity> targets;
        List<Relation> currentRelations;
        String typeCol;
        String idCol;
        if (param.getDirection() == 1) {
            typeCol = "entity_type";
            idCol = "entity_id";
        } else {
            typeCol = "related_entity_type";
            idCol = "related_entity_id";
        }
        QueryWrapper<Relation> wrapper = new QueryWrapper<Relation>()
                .eq(typeCol, param.getEntityType())
                .eq(idCol, param.getEntityId())
                .eq(param.getRelatedGroup() != -1, "related_group", param.getRelatedGroup())
                .orderBy(param.isSort(), param.asc(), CommonUtil.camelToUnderline(param.sortField));

        IPage<Relation> pages = mapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);
        if (pages.getRecords().isEmpty())
            return new SearchResult<>();
        Map<Integer, List<Relation>> groups = pages.getRecords().stream()
                .collect(Collectors.groupingBy(param.getDirection() == 1 ? Relation::getRelatedEntityType : Relation::getEntityType));
        for (int entityType : groups.keySet()) {
            if (entityType == EntityType.CHARACTER.getValue())
                continue;
            currentRelations = groups.get(entityType);
            targetIds = currentRelations.stream()
                    .map(param.getDirection() == 1 ? Relation::getRelatedEntityId : Relation::getEntityId)
                    .distinct().toList();
            subClass = entityUtil.getSubEntity(entityType);
            subMapper = MyBatisUtil.getMapper(subClass);
            targets = subMapper.selectByIds(targetIds);
            for (Relation r : currentRelations) {
                MetaEntity entity = DataFinder.findEntityById(param.getDirection() == 1 ? r.getRelatedEntityId() : r.getEntityId(), targets);
                if (entity == null) continue;
                Attribute<Long> role = DataFinder.findAttributeByValue(r.getRoleId(), MetaData.optionsZh.roleSet);
                Attribute<Long> reverseRole = DataFinder.findAttributeByValue(r.getReverseRoleId(), MetaData.optionsZh.roleSet);
                Attribute<Integer> relatedGroup = DataFinder.findAttributeByValue(r.getRelatedGroup().getValue(), MetaData.optionsZh.relatedGroupSet);
                if (role == null) role = new Attribute<>("-", 0L);
                if (reverseRole == null) reverseRole = new Attribute<>("-", 0L);
                res.add(
                        RelationVO.builder()
                                .id(r.getId())
                                .relatedGroup(relatedGroup)
                                .role(role)
                                .reverseRole(reverseRole)
                                .target(new Attribute<>(entity.getName(), entity.getId()))
                                .remark(r.getRemark())
                                .relatedTypeName(EntityType.getTableName(entityType))
                                .cover(resourceSrv.getThumbCover(entityType, param.getDirection() == 1 ? r.getRelatedEntityId() : r.getEntityId()))
                                .build()
                );
            }
        }
        return new SearchResult<>(res, pages.getTotal(), pages.getCurrent(), pages.getSize());
    }

    @Transactional
    public void addRelations(RelationCreateDTO dto) {
        List<Relation> res = new ArrayList<>();
        RelatedGroup group;
        switch (EntityType.get(dto.getRelatedEntityType())) {
            case EntityType.PERSON -> group = RelatedGroup.RELATED_PERSON;
            case EntityType.ENTRY -> group = RelatedGroup.RELATED_ENTRY;
            case EntityType.ITEM -> group = RelatedGroup.RELATED_ITEM;
            case EntityType.CHARACTER -> group = RelatedGroup.RELATED_CHAR;
            case EntityType.PRODUCT -> group = RelatedGroup.RELATED_PRODUCT;
            default -> group = RelatedGroup.DEFAULT;
        }
        for (Long targetId : dto.getRelatedEntityIds()) {
            Relation relation = new Relation();
            relation.setEntityType(dto.getEntityType());
            relation.setEntityId(dto.getEntityId());
            relation.setRoleId(dto.getRoleId());
            relation.setRelatedGroup(group);
            relation.setRelatedEntityType(dto.getRelatedEntityType());
            relation.setRelatedEntityId(targetId);
            res.add(relation);
        }
        //batch insert
        MybatisBatch.Method<Relation> method = new MybatisBatch.Method<>(RelationMapper.class);
        MybatisBatch<Relation> batchInsert = new MybatisBatch<>(sqlSessionFactory, res);
        batchInsert.execute(method.insert());
    }

    @Transactional
    public void updateRelation(RelationUpdateDTO dto) {
        mapper.update(
                new LambdaUpdateWrapper<Relation>()
                        .set(Relation::getRoleId, dto.getRoleId())
                        .set(Relation::getReverseRoleId, dto.getReverseRoleId())
                        .set(Relation::getRemark, dto.getRemark())
                        .eq(Relation::getId, dto.getId())
        );
    }

    @Transactional
    public void deleteRelations(List<Long> ids) {
        mapper.deleteByIds(ids);
    }

    @Transactional
    public ItemExcRelatedEntries getItemRelatedEntry(long entityId) {
        ItemExcRelatedEntries res = new ItemExcRelatedEntries();
        List<Relation> relations = mapper.selectList(
                new LambdaQueryWrapper<Relation>()
                        .in(Relation::getRelatedGroup, ItemUtil.ItemExcRelatedGroups)
                        .eq(Relation::getEntityType, EntityType.ITEM.getValue())
                        .eq(Relation::getEntityId, entityId)
        );
        if (relations.isEmpty())
            return res;
        List<Long> targetIds = relations.stream().map(Relation::getRelatedEntityId).distinct().toList();
        List<Entry> targets = entryMapper.selectByIds(targetIds);
        for (Relation r : relations) {
            Entry entry = DataFinder.findEntryById(r.getRelatedEntityId(), targets);
            if (entry == null) continue;
            RelationVO re = RelationVO.builder()
                    .target(new Attribute<>(entry.getName(), entry.getId()))
                    .remark(r.getRemark())
                    .build();
            if (entry.getType() == EntryType.CLASSIFICATION) {
                res.getClassifications().add(re);
            } else if (entry.getType() == EntryType.EVENT) {
                res.getEvents().add(re);
            } else if (entry.getType() == EntryType.MATERIAL) {
                res.getMaterials().add(re);
            }
        }
        return res;
    }

    @Transactional
    public SearchResult<ItemMiniVO> getRelatedItems(int entityType, long entityId, int page, int size) {
        SearchResult<ItemMiniVO> res = new SearchResult<>();
        IPage<Item> pages = itemMapper.selectJoinPage(new Page<>(page, size), Item.class,
                new MPJLambdaWrapper<Item>()
                        .selectAll(Item.class)
                        .innerJoin(Relation.class, Relation::getEntityId, Item::getId)
                        .eq(Relation::getEntityType, EntityType.ITEM.getValue())
                        .eq(Relation::getRelatedEntityType, entityType)
                        .eq(Relation::getRelatedEntityId, entityId)
                        .orderByDesc(Item::getReleaseDate));
        if (pages.getRecords().isEmpty())
            return res;
        List<ItemMiniVO> items = new ArrayList<>(converter.convert(pages.getRecords(), ItemMiniVO.class));
        List<Long> ids = items.stream().map(ItemMiniVO::getId).toList();
        List<Image> images = resourceSrv.getItemThumbAndCover(ids);
        images.sort(DataSorter.imageEntityTypeEntityIdTypeSorter);
        for (ItemMiniVO item : items) {
            Image thumb = DataFinder.findImageByEntityTypeEntityIdType(EntityType.ITEM.getValue(),
                    item.getId(), ImageType.THUMB.getValue(), images);
            item.setThumb(CommonImageUtil.getItemThumb(thumb));
            Image cover = DataFinder.findImageByEntityTypeEntityIdType(EntityType.ITEM.getValue(),
                    item.getId(), ImageType.MAIN.getValue(), images);
            item.setCover(CommonImageUtil.getItemCover(ItemType.get(item.getType().getValue()), cover));
        }
        return new SearchResult<>(items, pages.getTotal(), pages.getCurrent(), pages.getSize());
    }

}
