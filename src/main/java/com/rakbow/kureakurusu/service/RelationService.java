package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import com.rakbow.kureakurusu.data.SimpleSearchParam;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.emun.EntryType;
import com.rakbow.kureakurusu.data.emun.ImageType;
import com.rakbow.kureakurusu.data.emun.RelatedGroup;
import com.rakbow.kureakurusu.data.entity.Entity;
import com.rakbow.kureakurusu.data.entity.Entry;
import com.rakbow.kureakurusu.data.entity.Relation;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.data.result.ItemExtraInfo;
import com.rakbow.kureakurusu.data.vo.relation.RelatedEntityVO;
import com.rakbow.kureakurusu.data.vo.relation.RelationCreateMiniDTO;
import com.rakbow.kureakurusu.data.vo.relation.RelationVO;
import com.rakbow.kureakurusu.toolkit.*;
import com.rakbow.kureakurusu.toolkit.file.CommonImageUtil;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
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

    private final SqlSessionFactory sqlSessionFactory;
    private final Converter converter;
    private final EntryMapper entryMapper;
    private final ItemMapper itemMapper;
    private final ResourceService resourceSrv;

    private final List<EntryType> itemExtraInfoEntryTypes
            = List.of(EntryType.CLASSIFICATION, EntryType.MATERIAL, EntryType.EVENT);

    public SearchResult<RelatedEntityVO> getRelatedEntities(RelationQry qry) {
        int relatedGroup = qry.getRelatedGroup();
        int relatedEntity;
        int direction = qry.getDirection();
        List<RelatedEntityVO> res = new ArrayList<>();
        SimpleSearchParam param = new SimpleSearchParam(qry.getParam());
        IPage<Relation> pages = page(
                new Page<>(param.getPage(), param.getSize()),
                new LambdaQueryWrapper<Relation>()
                        .eq(Relation::getRelatedGroup, qry.getRelatedGroup())
                        .eq(direction == 1 ? Relation::getEntityType : Relation::getRelatedEntityType, qry.getEntityType())
                        .eq(direction == 1 ? Relation::getEntityId : Relation::getRelatedEntityId, qry.getEntityId())
                        .orderByAsc(param.getSize() == -1 ? Relation::getId : (direction == 1 ? Relation::getRoleId : Relation::getReverseRoleId))
        );
        if (pages.getRecords().isEmpty()) return new SearchResult<>();
        List<Relation> relations = pages.getRecords();
        if (relatedGroup == RelatedGroup.ITEM.getValue()) {
            relatedEntity = EntityType.ITEM.getValue();
        } else {
            relatedEntity = EntityType.ENTRY.getValue();
        }
        List<Long> targetIds = relations.stream().map(direction == 1 ? Relation::getRelatedEntityId : Relation::getEntityId).distinct().toList();
        List<Entry> targets = entryMapper.selectByIds(targetIds);
        for (Relation r : relations) {
            Entry e = DataFinder.findEntryById(direction == 1 ? r.getRelatedEntityId() : r.getEntityId(), targets);
            if (e == null) continue;
            Attribute<Long> role = DataFinder.findAttributeByValue(direction == 1 ? r.getRoleId() : r.getReverseRoleId(), MetaData.optionsZh.roleSet);
            if (role == null) continue;
            res.add(
                    RelatedEntityVO
                            .builder()
                            .type(relatedEntity)
                            .id(e.getId())
                            .name(e.getName())
                            .subName(EntryUtil.getSubName(e))
                            .cover(CommonImageUtil.getEntryThumb(e.getThumb()))
                            .role(role)
                            .build()
            );
        }
        return new SearchResult<>(res, pages.getTotal(), pages.getCurrent(), pages.getSize());
    }

    public List<RelationVO> getSimpleRelatedEntity(int direction, int relatedGroup, int entityType, long entityId) {
        String remark;
        List<RelationVO> res = new ArrayList<>();
        if (MetaData.optionsZh.roleSet.isEmpty())
            return res;
        List<Relation> relations = list(
                new LambdaQueryWrapper<Relation>()
                        .eq(Relation::getRelatedGroup, relatedGroup)
                        .eq(direction == 1 ? Relation::getEntityType : Relation::getRelatedEntityType, entityType)
                        .eq(direction == 1 ? Relation::getEntityId : Relation::getRelatedEntityId, entityId)
                        .orderByAsc(direction == 1 ? Relation::getRoleId : Relation::getReverseRoleId)
        );
        if (relations.isEmpty())
            return res;
        List<Long> targetIds = relations.stream().map(direction == 1 ? Relation::getRelatedEntityId : Relation::getEntityId).distinct().toList();
        List<Entry> targets = entryMapper.selectByIds(targetIds);
        for (Relation r : relations) {
            Entry e = DataFinder.findEntryById(direction == 1 ? r.getRelatedEntityId() : r.getEntityId(), targets);
            if (e == null) continue;
            if (e.getType() == EntryType.PRODUCT) {
                remark = I18nHelper.getMessage(e.getSubType().getLabelKey());
            } else {
                remark = r.getRemark();
            }
            Attribute<Long> role = DataFinder.findAttributeByValue(direction == 1 ? r.getRoleId() : r.getReverseRoleId(), MetaData.optionsZh.roleSet);
            if (role == null) continue;
            res.add(
                    RelationVO.builder()
                            .id(r.getId())
                            .role(role)
                            .target(new Attribute<>(e.getName(), e.getId()))
                            .remark(remark)
                            .thumb(CommonImageUtil.getEntryThumb(e.getThumb()))
                            .build()
            );
        }
        return res;
    }

    @Transactional
    @SneakyThrows
    public SearchResult<RelationVO> getRelations(RelationListQueryDTO param) {
        List<RelationVO> res = new ArrayList<>();
        List<Long> targetIds;
        BaseMapper<? extends Entity> subMapper;
        List<? extends Entity> targets;
        List<Relation> currentRelations;
        MPJLambdaWrapper<Relation> wrapper = new MPJLambdaWrapper<Relation>()
                .and(w ->
                        w.or(i -> i.eq(Relation::getRelatedEntityType, param.getEntityType()).eq(Relation::getRelatedEntityId, param.getEntityId()))
                                .or(i -> i.eq(Relation::getEntityType, param.getEntityType()).eq(Relation::getEntityId, param.getEntityId()))
                )
                .eq(ObjectUtils.isNotEmpty(param.getRelatedGroup()), Relation::getRelatedGroup, param.getRelatedGroup())
                .orderBy(param.isSort(), param.asc(), CommonUtil.camelToUnderline(param.getSortField()))
                .orderByDesc(Relation::getId);

        IPage<Relation> pages = page(new Page<>(param.getPage(), param.getSize()), wrapper);
        if (pages.getRecords().isEmpty())
            return new SearchResult<>();
        //mark direction
        pages.getRecords().forEach(r -> {
            if (r.getRelatedEntityType() == param.getEntityType()
                    && r.getRelatedEntityId() == param.getEntityId()) {
                r.setDirection(-1);
            }
        });
        //get map group by entity type
        Map<Integer, List<Relation>> groups = pages.getRecords().stream()
                .collect(Collectors.groupingBy(r -> r.getDirection() == 1 ? r.getRelatedEntityType() : r.getEntityType()));

        for (int entityType : groups.keySet()) {
            currentRelations = groups.get(entityType);
            targetIds = currentRelations.stream()
                    .map(r -> r.getDirection() == 1 ? r.getRelatedEntityId() : r.getEntityId())
                    .distinct().toList();

            if (entityType == EntityType.ITEM.getValue()) {
                subMapper = itemMapper;
            } else if (entityType == EntityType.ENTRY.getValue()) {
                subMapper = entryMapper;
            } else {
                throw new Exception();
            }
            targets = subMapper.selectByIds(targetIds);

            for (Relation r : currentRelations) {
                Entity e = DataFinder.findEntityById(r.getDirection() == 1 ? r.getRelatedEntityId() : r.getEntityId(), targets);
                if (e == null) continue;
                Attribute<Long> role = DataFinder.findAttributeByValue(r.getRoleId(), MetaData.optionsZh.roleSet);
                Attribute<Long> reverseRole = DataFinder.findAttributeByValue(r.getReverseRoleId(), MetaData.optionsZh.roleSet);
                Attribute<Integer> relatedGroup = new Attribute<>(
                        I18nHelper.getMessage(RelatedGroup.get(r.getRelatedGroup().getValue()).getLabelKey()),
                        r.getRelatedGroup().getValue()
                );
                if (role == null) role = new Attribute<>("-", 0L);
                if (reverseRole == null) reverseRole = new Attribute<>("-", 0L);

                RelationVO vo = RelationVO.builder()
                        .id(r.getId())
                        .relatedGroup(relatedGroup)
                        .role(role)
                        .reverseRole(reverseRole)
                        .target(new Attribute<>(e.getName(), e.getId()))
                        .remark(r.getRemark())
                        .build();
                if (entityType == EntityType.ENTRY.getValue()) {
                    vo.setTargetType(EntityType.ENTRY.getValue());
                    vo.setThumb(CommonImageUtil.getEntryThumb(((Entry) e).getThumb()));
                } else {
                    vo.setTargetType(EntityType.ITEM.getValue());
                    vo.setThumb(resourceSrv.getEntityImageCache(EntityType.ITEM.getValue(), e.getId(), ImageType.MAIN));
                }
                res.add(vo);
            }
        }
        return new SearchResult<>(res, pages.getTotal(), pages.getCurrent(), pages.getSize());
    }

    @Transactional
    public void addRelatedEntries(RelationCreateDTO dto) {
        List<Relation> res = new ArrayList<>();
        for (RelationCreateMiniDTO target : dto.getRelatedEntries()) {
            Relation relation = new Relation();
            relation.setEntityType(dto.getEntityType());
            relation.setEntityId(dto.getEntityId());
            relation.setRoleId(dto.getRoleId());
            relation.setRelatedGroup(RelatedGroup.get(dto.getRelatedGroup()));
            relation.setRelatedEntityType(EntityType.ENTRY.getValue());
            relation.setRelatedEntityId(target.getId());
            relation.setRemark(target.getRemark());
            res.add(relation);
        }
        //batch insert
        MybatisBatch.Method<Relation> method = new MybatisBatch.Method<>(RelationMapper.class);
        MybatisBatch<Relation> batchInsert = new MybatisBatch<>(sqlSessionFactory, res);
        batchInsert.execute(method.insert());
    }

    @Transactional
    public void batchCreate(int entityType, long entityId, List<RelatedEntityMiniDTO> relatedEntities) {
        List<Relation> relations = converter.convert(relatedEntities, Relation.class);
        relations.forEach(r -> {
            r.setEntityType(entityType);
            r.setEntityId(entityId);
        });
        //batch insert
        MybatisBatch.Method<Relation> method = new MybatisBatch.Method<>(RelationMapper.class);
        MybatisBatch<Relation> batchInsert = new MybatisBatch<>(sqlSessionFactory, relations);
        batchInsert.execute(method.insert());
    }

    @Transactional
    public void updateRelation(RelationUpdateDTO dto) {
        update(
                new LambdaUpdateWrapper<Relation>()
                        .set(Relation::getRoleId, dto.getRoleId())
                        .set(Relation::getReverseRoleId, dto.getReverseRoleId())
                        .set(Relation::getRemark, dto.getRemark())
                        .eq(Relation::getId, dto.getId())
        );
    }

    @Transactional
    public ItemExtraInfo getItemExtraInfo(long id) {
        ItemExtraInfo res = new ItemExtraInfo();
        List<Relation> relations = list(
                new LambdaQueryWrapper<Relation>()
                        .in(Relation::getRelatedGroup, ItemUtil.ItemExcRelatedGroups)
                        .eq(Relation::getEntityType, EntityType.ITEM.getValue())
                        .eq(Relation::getEntityId, id)
        );
        if (relations.isEmpty())
            return res;
        List<Long> entryIds = relations.stream().map(Relation::getRelatedEntityId).distinct().toList();
        List<Entry> entries = entryMapper.selectList(new LambdaQueryWrapper<Entry>()
                        .in(Entry::getType, itemExtraInfoEntryTypes).in(Entry::getId, entryIds));
        for (Relation r : relations) {
            Entry e = DataFinder.findEntryById(r.getRelatedEntityId(), entries);
            if (e == null) continue;
            RelationVO re = RelationVO.builder()
                    .target(new Attribute<>(e.getName(), e.getId()))
                    .remark(r.getRemark())
                    .build();
            if (e.getType() == EntryType.CLASSIFICATION) {
                res.getClassifications().add(re);
            } else if (e.getType() == EntryType.EVENT) {
                res.getEvents().add(re);
            } else if (e.getType() == EntryType.MATERIAL) {
                res.getMaterials().add(re);
            }
        }
        return res;
    }

}
