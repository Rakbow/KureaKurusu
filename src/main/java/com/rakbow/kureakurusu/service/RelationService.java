package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.EntryMapper;
import com.rakbow.kureakurusu.dao.ItemMapper;
import com.rakbow.kureakurusu.dao.RelationMapper;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.RelatedEntityMiniDTO;
import com.rakbow.kureakurusu.data.dto.RelationCreateDTO;
import com.rakbow.kureakurusu.data.dto.RelationListQueryDTO;
import com.rakbow.kureakurusu.data.dto.RelationUpdateDTO;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.emun.EntryType;
import com.rakbow.kureakurusu.data.emun.ImageType;
import com.rakbow.kureakurusu.data.emun.ItemType;
import com.rakbow.kureakurusu.data.entity.Entity;
import com.rakbow.kureakurusu.data.entity.Entry;
import com.rakbow.kureakurusu.data.entity.Relation;
import com.rakbow.kureakurusu.data.entity.item.Item;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.data.vo.relation.PersonVO;
import com.rakbow.kureakurusu.data.vo.relation.Personnel;
import com.rakbow.kureakurusu.data.vo.relation.RelationTargetVO;
import com.rakbow.kureakurusu.data.vo.relation.RelationVO;
import com.rakbow.kureakurusu.toolkit.DataFinder;
import com.rakbow.kureakurusu.toolkit.EntityUtil;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import com.rakbow.kureakurusu.toolkit.file.CommonImageUtil;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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

    private final RelationMapper mapper;
    private final EntryMapper entryMapper;
    private final ItemMapper itemMapper;

    private final ImageService imageSrv;
    private final EntityUtil entityUtil;

    @Transactional
    @SneakyThrows
    public SearchResult<RelationVO> list(RelationListQueryDTO dto) {
        dto.init();
        long start = System.currentTimeMillis();
        int targetEntityType = dto.getTargetEntityType();
        List<RelationVO> res = new ArrayList<>();
        List<Relation> relations = mapper.list(dto);
        if (relations.isEmpty()) return new SearchResult<>();
        long total = mapper.count(dto);

        List<Attribute<Long>> roleSet = MetaData.getOptions().roleSet;

        //mark direction
        relations.forEach(r -> {
            if (r.getRelatedEntityType() == dto.getEntityType()
                    && r.getRelatedEntityId() == dto.getEntityId()) {
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
            boolean positive = r.getDirection() == 1;

            Entity e = DataFinder.findEntityById(positive ? r.getRelatedEntityId() : r.getEntityId(), targets);
            if (e == null) continue;

            Long roleId = positive ? r.getRoleId() : r.getRelatedRoleId();
            Long targetRoleId = positive ? r.getRelatedRoleId() : r.getRoleId();
            Attribute<Long> role = DataFinder.findAttributeByValue(roleId, roleSet);
            Attribute<Long> targetRole = DataFinder.findAttributeByValue(targetRoleId, roleSet);

            int subTypeValue = positive ? r.getRelatedEntitySubType() : r.getEntitySubType();
            String subTypeLabel;
            if (targetEntityType == EntityType.ENTRY.getValue()) {
                subTypeLabel = I18nHelper.getMessage(EntryType.get(subTypeValue).getLabelKey());
            } else {
                subTypeLabel = I18nHelper.getMessage(ItemType.get(subTypeValue).getLabelKey());
            }
            Attribute<Integer> targetSubType = new Attribute<>(subTypeLabel, subTypeValue);
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
                target.setThumb(imageSrv.getCache(EntityType.ITEM.getValue(), e.getId(), ImageType.MAIN));
                target.setName(((Item) e).getName());
                target.setSubName(((Item) e).getBarcode());
            }

            RelationVO vo = RelationVO.builder()
                    .id(r.getId())
                    .role(role)
                    .role(targetRole)
                    .remark(r.getRemark())
                    .target(target)
                    .direction(positive)
                    .build();

            res.add(vo);
        }
        return new SearchResult<>(res, total, start);
    }

    @Transactional
    public void create(RelationCreateDTO dto) {
        List<Relation> res = new ArrayList<>();
        dto.getRelatedEntries().forEach(r ->
                res.add(Relation.builder()
                        .roleId(dto.getRelatedRoleId())
                        .relatedRoleId(dto.getRoleId())
                        .entityType(dto.getEntityType())
                        .entitySubType(dto.getEntitySubType())
                        .entityId(dto.getEntityId())
                        .relatedEntityType(dto.getRelatedEntityType())
                        .relatedEntitySubType(dto.getRelatedEntitySubType())
                        .relatedEntityId(r.getId())
                        .remark(r.getRemark())
                        .build()));
        //batch insert
        MybatisBatch.Method<Relation> method = new MybatisBatch.Method<>(RelationMapper.class);
        MybatisBatch<Relation> batchInsert = new MybatisBatch<>(sqlSessionFactory, res);
        batchInsert.execute(method.insert());
    }

    @Transactional
    public void batchCreate(int entityType, long entityId, int entitySubType, List<RelatedEntityMiniDTO> relatedEntities) {
        List<Relation> relations = converter.convert(relatedEntities, Relation.class);
        relations.forEach(r -> {
            r.setEntityType(entityType);
            r.setEntitySubType(entitySubType);
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
                        .set(Relation::getRoleId, dto.getDirection() ? dto.getRoleId() : dto.getRelatedRoleId())
                        .set(Relation::getRelatedRoleId, dto.getDirection() ? dto.getRelatedRoleId() : dto.getRoleId())
                        .set(Relation::getRemark, dto.getRemark())
                        .eq(Relation::getId, dto.getId())
        );
    }

    @Transactional
    public List<Personnel> getPersonnel(int entityType, long entityId) {
        List<Personnel> res = new ArrayList<>();

        RelationListQueryDTO param = new RelationListQueryDTO();
        param.getFilters().computeIfAbsent("entityType", _ -> new LinkedHashMap<>()).put("value", entityType);
        param.getFilters().computeIfAbsent("entityId", _ -> new LinkedHashMap<>()).put("value", (int) entityId);
        param.getFilters().computeIfAbsent("targetEntityType", _ -> new LinkedHashMap<>()).put("value", EntityType.ENTRY.getValue());
        param.getFilters().computeIfAbsent("targetEntitySubTypes", _ -> new LinkedHashMap<>()).put("value", List.of(EntryType.PERSON.getValue()));

        SearchResult<RelationVO> relations = list(param);
        if (relations.data.isEmpty()) return res;

        Map<Attribute<Long>, List<RelationVO>> relationGroup = relations.data.stream()
                .collect(Collectors.groupingBy(r -> r.getTarget().getRole()));

        res = relationGroup.entrySet().stream()
                .map(entry -> {
                    Personnel pl = new Personnel();
                    pl.setRole(entry.getKey());
                    pl.setPersons(entry.getValue().stream().map(r -> {
                        PersonVO p = new PersonVO();
                        p.setId(r.getTarget().getEntityId());
                        p.setName(r.getTarget().getName());
                        p.setSubName(r.getTarget().getSubName());
                        p.setRemark(r.getRemark());
                        return p;
                    }).collect(Collectors.toList()));
                    return pl;
                })
                .toList();
        return res;
    }

}
