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
import com.rakbow.kureakurusu.data.dto.RelatedEntityMiniDTO;
import com.rakbow.kureakurusu.data.dto.RelationCreateDTO;
import com.rakbow.kureakurusu.data.dto.RelationListQueryDTO;
import com.rakbow.kureakurusu.data.dto.RelationUpdateDTO;
import com.rakbow.kureakurusu.data.emun.*;
import com.rakbow.kureakurusu.data.entity.Entity;
import com.rakbow.kureakurusu.data.entity.Entry;
import com.rakbow.kureakurusu.data.entity.Relation;
import com.rakbow.kureakurusu.data.entity.item.Item;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.data.result.ItemExtraInfo;
import com.rakbow.kureakurusu.data.vo.relation.RelationCreateMiniDTO;
import com.rakbow.kureakurusu.data.vo.relation.RelationTargetVO;
import com.rakbow.kureakurusu.data.vo.relation.RelationVO;
import com.rakbow.kureakurusu.toolkit.*;
import com.rakbow.kureakurusu.toolkit.file.CommonImageUtil;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    private final EntityUtil entityUtil;

    private final List<EntryType> itemExtraInfoEntryTypes
            = List.of(EntryType.CLASSIFICATION, EntryType.MATERIAL, EntryType.EVENT);

    @Transactional
    @SneakyThrows
    public SearchResult<RelationVO> list(RelationListQueryDTO param) {
        int targetEntityType = param.getTargetEntityType();
        List<RelationVO> res = new ArrayList<>();
        MPJLambdaWrapper<Relation> wrapper = new MPJLambdaWrapper<Relation>()
                .and(w -> w.or(i -> i.eq(Relation::getRelatedEntityType, param.getEntityType())
                                .eq(Relation::getRelatedEntityId, param.getEntityId())
                                .eq(Relation::getEntityType, targetEntityType)
                                .in(!param.getTargetEntitySubTypes().isEmpty(), Relation::getEntitySubType, param.getTargetEntitySubTypes()))
                        .or(i -> i.eq(Relation::getEntityType, param.getEntityType())
                                .eq(Relation::getEntityId, param.getEntityId())
                                .eq(Relation::getRelatedEntityType, targetEntityType)
                                .in(!param.getTargetEntitySubTypes().isEmpty(), Relation::getRelatedEntitySubType, param.getTargetEntitySubTypes()))
                )
                .orderByAsc(!param.isSort(), Relation::getRelatedEntityId, Relation::getEntityId)
                .orderBy(param.isSort(), param.asc(), CommonUtil.camelToUnderline(param.getSortField()));

        IPage<Relation> pages = page(new Page<>(param.getPage(), param.getSize()), wrapper);
        if (pages.getRecords().isEmpty())
            return new SearchResult<>();
        List<Relation> relations = pages.getRecords();
        List<Attribute<Long>> roleSet = MetaData.getOptions().roleSet;

        //mark direction
        relations.forEach(r -> {
            if (r.getRelatedEntityType() == param.getEntityType()
                    && r.getRelatedEntityId() == param.getEntityId()) {
                r.setDirection(-1);
            }
        });


        List<Long> targetIds = relations.stream()
                .map(r -> r.getDirection() == 1 ? r.getRelatedEntityId() : r.getEntityId()).distinct().toList();
        BaseMapper<? extends Entity> subMapper;
        if (targetEntityType == EntityType.ITEM.getValue()) {
            subMapper = itemMapper;
        } else if (targetEntityType == EntityType.ENTRY.getValue()) {
            subMapper = entryMapper;
        } else {
            throw new Exception();
        }
        List<? extends Entity> targets = subMapper.selectByIds(targetIds);

        for (Relation r : relations) {
            Entity e = DataFinder.findEntityById(r.getDirection() == 1 ? r.getRelatedEntityId() : r.getEntityId(), targets);
            if (e == null) continue;
            Attribute<Long> role;
            Attribute<Long> targetRole;
            Attribute<Integer> targetSubType;

            String subTypeLabel;
            Integer subTypeValue;

            if (r.getDirection() == 1) {
                role = DataFinder.findAttributeByValue(r.getRoleId(), roleSet);
                targetRole = DataFinder.findAttributeByValue(r.getRelatedRoleId(), roleSet);

                if (targetEntityType == EntityType.ENTRY.getValue()) {
                    subTypeLabel = I18nHelper.getMessage(EntrySubType.get(r.getRelatedEntitySubType()).getLabelKey());
                    subTypeValue = EntrySubType.get(r.getRelatedEntitySubType()).getValue();
                } else {
                    subTypeLabel = I18nHelper.getMessage(ItemSubType.get(r.getRelatedEntitySubType()).getLabelKey());
                    subTypeValue = ItemSubType.get(r.getRelatedEntitySubType()).getValue();
                }

            } else {
                role = DataFinder.findAttributeByValue(r.getRelatedRoleId(), roleSet);
                targetRole = DataFinder.findAttributeByValue(r.getRoleId(), roleSet);

                if (targetEntityType == EntityType.ENTRY.getValue()) {
                    subTypeLabel = I18nHelper.getMessage(EntrySubType.get(r.getEntitySubType()).getLabelKey());
                    subTypeValue = EntrySubType.get(r.getEntitySubType()).getValue();
                } else {
                    subTypeLabel = I18nHelper.getMessage(ItemSubType.get(r.getEntitySubType()).getLabelKey());
                    subTypeValue = ItemSubType.get(r.getEntitySubType()).getValue();
                }

            }
            targetSubType = new Attribute<>(subTypeLabel, subTypeValue);
            if (targetRole == null) targetRole = new Attribute<>("-", 0L);
            if (role == null) role = new Attribute<>("-", 0L);


            RelationTargetVO target = RelationTargetVO.builder()
                    .entityType(targetEntityType)
                    .entityId(e.getId())
                    .role(targetRole)
                    .subType(targetSubType)
                    .build();

            if (targetEntityType == EntityType.ENTRY.getValue()) {
                target.setThumb(CommonImageUtil.getEntryThumb(((Entry) e).getThumb()));
                target.setName(((Entry) e).getName());
                target.setSubName(entityUtil.getSubName(((Entry) e).getNameZh(), ((Entry) e).getNameEn()));
            } else {
                target.setThumb(resourceSrv.getEntityImageCache(EntityType.ITEM.getValue(), e.getId(), ImageType.MAIN));
                target.setName(((Item) e).getName());
                target.setSubName(((Item) e).getBarcode());
            }

            RelationVO vo = RelationVO.builder()
                    .id(r.getId())
                    .role(role)
                    .role(targetRole)
                    .remark(r.getRemark())
                    .target(target)
                    .build();

            res.add(vo);
        }
        return new SearchResult<>(res, pages.getTotal(), pages.getCurrent(), pages.getSize());
    }

    @Transactional
    public void create(RelationCreateDTO dto) {
        List<Relation> res = new ArrayList<>();
        for (RelationCreateMiniDTO target : dto.getRelatedEntries()) {
            Relation relation = new Relation();

            relation.setRoleId(dto.getRoleId());

            relation.setEntityType(dto.getEntityType());
            relation.setEntitySubType(dto.getEntitySubType());
            relation.setEntityId(dto.getEntityId());

            relation.setRelatedEntityType(dto.getRelatedEntityType());
            relation.setRelatedEntitySubType(dto.getRelatedEntitySubType());
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
    public void update(RelationUpdateDTO dto) {
        update(
                new LambdaUpdateWrapper<Relation>()
                        .set(Relation::getRoleId, dto.getRoleId())
                        .set(Relation::getRelatedRoleId, dto.getReverseRoleId())
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
                    .target(
                            RelationTargetVO.builder()
                                    .entityType(EntityType.ENTRY.getValue())
                                    .entityId(e.getId())
                                    .name(e.getName())
                                    .build()
                    )
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
