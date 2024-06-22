package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.PersonMapper;
import com.rakbow.kureakurusu.dao.RelationMapper;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.dto.RelationCreateDTO;
import com.rakbow.kureakurusu.data.dto.RelationUpdateDTO;
import com.rakbow.kureakurusu.data.emun.RoleGroup;
import com.rakbow.kureakurusu.data.entity.Person;
import com.rakbow.kureakurusu.data.entity.Relation;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.data.vo.relation.RelationVO;
import com.rakbow.kureakurusu.toolkit.DataFinder;
import lombok.RequiredArgsConstructor;
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

    private final RelationMapper mapper;
    private final PersonMapper personMapper;
    private final SqlSessionFactory sqlSessionFactory;

    public List<RelationVO> getRelations(int roleGroup, int entityType, long entityId) {
        List<RelationVO> res = new ArrayList<>();
        if (MetaData.optionsZh.roleSet.isEmpty())
            return res;
        List<Relation> relations = mapper.selectList(
                new LambdaQueryWrapper<Relation>()
                        .eq(Relation::getRoleGroup, roleGroup)
                        .eq(Relation::getEntityType, entityType)
                        .eq(Relation::getEntityId, entityId)
                        .orderByAsc(Relation::getRoleId)
        );
        if (relations.isEmpty())
            return res;
        List<Long> targetIds = relations.stream().map(Relation::getRelatedEntityId).distinct().toList();

        if (roleGroup == RoleGroup.RELATED_PERSON.getValue()) {
            List<Person> persons = personMapper.selectBatchIds(targetIds);

            for (Relation r : relations) {
                Person person = DataFinder.findPersonById(r.getRelatedEntityId(), persons);
                if (person == null) continue;
                Attribute<Long> role = DataFinder.findAttributeByValue(r.getRoleId(), MetaData.optionsZh.roleSet);
                if (role == null) continue;
                res.add(
                        RelationVO.builder()
                                .id(r.getId())
                                .role(role)
                                .target(new Attribute<>(person.getName(), person.getId()))
                                .remark(r.getRemark())
                                .build()
                );
            }
        }

        return res;
    }

    @Transactional
    public void addRelations(RelationCreateDTO dto) {
        List<Relation> res = new ArrayList<>();
        for (Long targetId : dto.getRelatedEntityIds()) {
            Relation relation = new Relation();
            relation.setEntityType(dto.getEntityType());
            relation.setEntityId(dto.getEntityId());
            relation.setRoleId(dto.getRoleId());
            relation.setRoleGroup(RoleGroup.get(dto.getRoleGroup()));
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
                        .set(Relation::getRemark, dto.getRemark())
                        .eq(Relation::getId, dto.getId())
        );
    }

    @Transactional
    public void deleteRelations(List<Long> ids) {
        mapper.deleteByIds(ids);
    }

}
