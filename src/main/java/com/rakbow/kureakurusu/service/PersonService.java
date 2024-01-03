package com.rakbow.kureakurusu.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rakbow.kureakurusu.dao.PersonMapper;
import com.rakbow.kureakurusu.dao.PersonRelationMapper;
import com.rakbow.kureakurusu.dao.PersonRoleMapper;
import com.rakbow.kureakurusu.data.*;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.dto.person.PersonUpdateDTO;
import com.rakbow.kureakurusu.data.dto.person.PersonnelManageCmd;
import com.rakbow.kureakurusu.data.emun.system.DataActionType;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.data.person.Personnel;
import com.rakbow.kureakurusu.data.person.PersonnelPair;
import com.rakbow.kureakurusu.data.person.PersonnelStruct;
import com.rakbow.kureakurusu.data.vo.person.PersonMiniVO;
import com.rakbow.kureakurusu.data.vo.person.PersonVOBeta;
import com.rakbow.kureakurusu.entity.Person;
import com.rakbow.kureakurusu.entity.PersonRelation;
import com.rakbow.kureakurusu.entity.PersonRole;
import com.rakbow.kureakurusu.util.common.DataFinder;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.common.SpringUtil;
import com.rakbow.kureakurusu.util.convertMapper.entity.PersonVOMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.rakbow.kureakurusu.data.common.Constant.*;

/**
 * @author Rakbow
 * @since 2023-11-14 20:47
 */
@Service
public class PersonService {

    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

    private final PersonVOMapper voMapper = PersonVOMapper.INSTANCES;
    private final SqlSessionFactory sqlSessionFactory = SpringUtil.getBean("sqlSessionFactory");

    @Resource
    private PersonMapper mapper;
    @Resource
    private PersonRoleMapper roleMapper;
    @Resource
    private PersonRelationMapper relationMapper;

    //region person
    public Person getPerson(long id) {
        return mapper.selectById(id);
    }

    public void addPerson(Person person) {
        mapper.insert(person);
    }

    public void updatePerson(PersonUpdateDTO dto) {

        LambdaUpdateWrapper<Person> wrapper = new LambdaUpdateWrapper<Person>()
                .eq(Person::getId, dto.getId())
                .set(Person::getName, dto.getName())
                .set(Person::getNameZh, dto.getNameZh())
                .set(Person::getNameEn, dto.getNameEn())
                .set(Person::getGender, dto.getGender().getValue())
                .set(Person::getBirthDate, dto.getBirthDate())
                .set(Person::getAliases, JSON.toJSONString(dto.getAliases()))
                .set(Person::getRemark, dto.getRemark())
                .set(Person::getEditedTime, DateHelper.NOW_TIMESTAMP);

        mapper.update(null, wrapper);
    }

    public void deletePerson(Person person) {
        mapper.deleteById(person);
    }

    public SearchResult getPersons(QueryParams param) {

        String name = param.getString("name");
        String nameZh = param.getString("nameZh");
        String nameEn = param.getString("nameEn");
        String aliases = param.getString("aliases");

        LambdaQueryWrapper<Person> wrapper = new LambdaQueryWrapper<Person>()
                .apply(!StringUtils.isBlank(aliases), "JSON_UNQUOTE(JSON_EXTRACT(aliases, '$[*]')) LIKE concat('%', {0}, '%')", aliases)
                .like(!StringUtils.isBlank(name), Person::getName, name)
                .like(!StringUtils.isBlank(nameZh), Person::getNameZh, nameZh)
                .like(!StringUtils.isBlank(nameEn), Person::getNameEn, nameEn);

        List<Integer> gender = param.getArray("gender", Integer.class);
        if (gender != null && !gender.isEmpty()) {
            wrapper.in(Person::getGender, gender);
        }

        if (!StringUtils.isBlank(param.sortField)) {
            switch (param.sortField) {
                case "name" -> wrapper.orderBy(true, param.sortOrder == 1, Person::getName);
                case "nameZh" -> wrapper.orderBy(true, param.sortOrder == 1, Person::getNameZh);
                case "nameEn" -> wrapper.orderBy(true, param.sortOrder == 1, Person::getNameEn);
                case "birthDate" -> wrapper.orderBy(true, param.sortOrder == 1, Person::getBirthDate);
                case "gender" -> wrapper.orderBy(true, param.sortOrder == 1, Person::getGender);
            }
        }

        IPage<Person> pages = mapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);

        List<PersonVOBeta> persons = voMapper.toBetaVO(pages.getRecords());

        return new SearchResult(persons, pages);
    }

    /**
     * 搜索person
     *
     * @param param 参数
     * @author rakbow
     */
    public SearchResult searchPersons(SimpleSearchParam param) {

        LambdaQueryWrapper<Person> wrapper = new LambdaQueryWrapper<Person>()
                .and(i -> i.apply("JSON_UNQUOTE(JSON_EXTRACT(aliases, '$[*]')) LIKE concat('%', {0}, '%')", param.getKeyword()))
                .or().like(Person::getName, param.getKeyword())
                .or().like(Person::getNameZh, param.getKeyword())
                .or().like(Person::getNameEn, param.getKeyword())
                .eq(Person::getStatus, 1)
                .orderByDesc(Person::getId);

        IPage<Person> pages = mapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);

        List<PersonMiniVO> persons = voMapper.toMiniVO(pages.getRecords());

        return new SearchResult(persons, pages);
    }
    //endregion

    //region person role

    public void addRole(PersonRole role) {
        roleMapper.insert(role);
    }

    public void updateRole(PersonRole role) {
        roleMapper.updateById(role);
    }

    public SearchResult getRoles(QueryParams param) {
        String name = param.getString("name");
        String nameZh = param.getString("nameZh");
        String nameEn = param.getString("nameEn");

        LambdaQueryWrapper<PersonRole> wrapper = new LambdaQueryWrapper<PersonRole>()
                .like(!StringUtils.isBlank(name), PersonRole::getName, name)
                .like(!StringUtils.isBlank(nameZh), PersonRole::getNameZh, nameZh)
                .like(!StringUtils.isBlank(nameEn), PersonRole::getNameEn, nameEn);
        if (!StringUtils.isBlank(param.sortField)) {
            switch (param.sortField) {
                case "name" -> wrapper.orderBy(true, param.sortOrder == 1, PersonRole::getName);
                case "nameZh" -> wrapper.orderBy(true, param.sortOrder == 1, PersonRole::getNameZh);
                case "nameEn" -> wrapper.orderBy(true, param.sortOrder == 1, PersonRole::getNameEn);
            }
        } else {
            wrapper.orderByDesc(PersonRole::getId);
        }

        IPage<PersonRole> pages = roleMapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);

        return new SearchResult(pages);
    }

    //endregion

    //region relation

    public PersonnelStruct getPersonnel(int entityType, long entityId) {
        PersonnelStruct res = new PersonnelStruct();
        if (MetaData.optionsZh.roleSet.isEmpty())
            return res;
        List<PersonRelation> relations = relationMapper.selectList(
                new LambdaQueryWrapper<PersonRelation>()
                        .eq(PersonRelation::getEntityType, entityType)
                        .eq(PersonRelation::getEntityId, entityId)
        );
        if (relations.isEmpty())
            return res;
        List<Long> personIds = relations.stream().map(PersonRelation::getPersonId).distinct().toList();
        List<Person> persons = mapper.selectBatchIds(personIds);
        Map<Long, List<PersonRelation>> relationGroup = relations.stream()
                .collect(Collectors.groupingBy(PersonRelation::getRoleId));

        Map<Long, List<PersonRelation>> sortedRelationGroup = relationGroup.entrySet().stream()
                .sorted(Comparator.comparingInt(entry ->
                        entry.getValue().isEmpty() ? Integer.MAX_VALUE :
                                entry.getValue().get(0).getId().intValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> b, // 如果有重复的键，保留后者
                        LinkedHashMap::new // 保持插入顺序
                ));

        for(Long roleId : sortedRelationGroup.keySet()) {
            Personnel personnel = new Personnel();
            Attribute<Long> role = DataFinder.findAttributeByValue(roleId, MetaData.optionsZh.roleSet);
            if(role == null) continue;
            personnel.setRole(role);
            List<PersonRelation> relationSet = relationGroup.get(roleId);
            for(PersonRelation r : relationSet) {
                Person person = DataFinder.findPersonById(r.getPersonId(), persons);
                if(person == null) continue;

                PersonnelPair pair = new PersonnelPair();
                pair.setId(r.getId());
                pair.setMain(r.isMain());
                pair.setRole(role);
                pair.setPerson(new Attribute<>(person.getName() + SLASH_WITH_SPACE + person.getNameZh(), person.getId()));
                res.addEditPersonnel(pair);

                Attribute<Long> p = new Attribute<>(person.getNameZh(), person.getId());
                personnel.getPersons().add(p);
            }
            res.addPersonnel(personnel);
        }
        return res;
    }

    public void managePersonnel(PersonnelManageCmd cmd) {

        List<PersonRelation> addRelationSet = new ArrayList<>();
        List<PersonRelation> deleteRelationSet = new ArrayList<>();

        cmd.getPersonnel().forEach(pair -> {
            if(pair.getAction() == DataActionType.INSERT.getValue())
                addRelationSet.add(new PersonRelation(pair, cmd.getEntityType(), cmd.getEntityId()));
            if(pair.getAction() == DataActionType.REAL_DELETE.getValue())
                deleteRelationSet.add(new PersonRelation(pair, cmd.getEntityType(), cmd.getEntityId()));
        });

        //批量删除和批量新增
        MybatisBatch.Method<PersonRelation> method = new MybatisBatch.Method<>(PersonRelationMapper.class);
        MybatisBatch<PersonRelation> batchInsert = new MybatisBatch<>(sqlSessionFactory, addRelationSet);
        MybatisBatch<PersonRelation> batchDelete = new MybatisBatch<>(sqlSessionFactory, deleteRelationSet);
        batchInsert.execute(method.insert());
        batchDelete.execute(method.deleteById());

    }

    //endregion
}
