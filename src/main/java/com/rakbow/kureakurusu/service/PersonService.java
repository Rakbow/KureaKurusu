package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.PersonMapper;
import com.rakbow.kureakurusu.dao.PersonRelationMapper;
import com.rakbow.kureakurusu.dao.PersonRoleMapper;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.SimpleSearchParam;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.dto.person.PersonDetailQry;
import com.rakbow.kureakurusu.data.dto.person.PersonUpdateDTO;
import com.rakbow.kureakurusu.data.dto.person.PersonnelManageCmd;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.emun.system.DataActionType;
import com.rakbow.kureakurusu.data.entity.Person;
import com.rakbow.kureakurusu.data.entity.PersonRelation;
import com.rakbow.kureakurusu.data.entity.PersonRole;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.data.person.Personnel;
import com.rakbow.kureakurusu.data.person.PersonnelPair;
import com.rakbow.kureakurusu.data.person.PersonnelStruct;
import com.rakbow.kureakurusu.data.vo.person.PersonDetailVO;
import com.rakbow.kureakurusu.data.vo.person.PersonMiniVO;
import com.rakbow.kureakurusu.data.vo.person.PersonVOBeta;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.CommonUtil;
import com.rakbow.kureakurusu.util.common.DataFinder;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.common.EntityUtil;
import com.rakbow.kureakurusu.util.convertMapper.entity.PersonVOMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.rakbow.kureakurusu.data.common.Constant.*;

/**
 * @author Rakbow
 * @since 2023-11-14 20:47
 */
@Service
@RequiredArgsConstructor
public class PersonService extends ServiceImpl<PersonMapper, Person> {

    private final PersonVOMapper VOMapper;
    private final PersonMapper mapper;
    private final PersonRoleMapper roleMapper;
    private final PersonRelationMapper relationMapper;
    private final EntityUtil entityUtil;
    private final SqlSessionFactory sqlSessionFactory;
    private final int ENTITY_VALUE = Entity.PERSON.getValue();

    @SneakyThrows
    public PersonDetailVO detail(PersonDetailQry qry) {
        Person person = getById(qry.getId());
        if (person == null)
            throw new Exception(I18nHelper.getMessage("entity.url.error", Entity.PERSON.getName()));

        return PersonDetailVO.builder()
                .item(VOMapper.toVO(person))
                .traffic(entityUtil.getPageTraffic(ENTITY_VALUE, qry.getId()))
                .options(entityUtil.getDetailOptions(ENTITY_VALUE))
                .build();
    }

    public void updatePerson(PersonUpdateDTO dto) {

        LambdaUpdateWrapper<Person> wrapper = new LambdaUpdateWrapper<Person>()
                .eq(Person::getId, dto.getId())
                .set(Person::getName, dto.getName())
                .set(Person::getNameZh, dto.getNameZh())
                .set(Person::getNameEn, dto.getNameEn())
                .set(Person::getGender, dto.getGender().getValue())
                .set(Person::getBirthDate, dto.getBirthDate())
                .set(Person::getAliases, CommonUtil.getListStr(dto.getAliases()))
                .set(Person::getRemark, dto.getRemark())
                .set(Person::getAddedTime, dto.getAddedTime())
                .set(Person::getEditedTime, DateHelper.now());

        mapper.update(null, wrapper);
    }

    public SearchResult<PersonVOBeta> getPersons(QueryParams param) {

        String name = param.getStr("name");
        String nameZh = param.getStr("nameZh");
        String nameEn = param.getStr("nameEn");
        String aliases = param.getStr("aliases");

        LambdaQueryWrapper<Person> wrapper = new LambdaQueryWrapper<Person>()
                .apply(!StringUtils.isBlank(aliases), "JSON_UNQUOTE(JSON_EXTRACT(aliases, '$[*]')) LIKE concat('%', {0}, '%')", aliases)
                .like(!StringUtils.isBlank(name), Person::getName, name)
                .like(!StringUtils.isBlank(nameZh), Person::getNameZh, nameZh)
                .like(!StringUtils.isBlank(nameEn), Person::getNameEn, nameEn);

        List<Integer> gender = param.getArray("gender");
        if (gender != null && !gender.isEmpty()) {
            wrapper.in(Person::getGender, gender);
        }

        if (!StringUtils.isBlank(param.sortField)) {
            switch (param.sortField) {
                case "name" -> wrapper.orderBy(true, param.asc(), Person::getName);
                case "nameZh" -> wrapper.orderBy(true, param.asc(), Person::getNameZh);
                case "nameEn" -> wrapper.orderBy(true, param.asc(), Person::getNameEn);
                case "birthDate" -> wrapper.orderBy(true, param.asc(), Person::getBirthDate);
                case "gender" -> wrapper.orderBy(true, param.asc(), Person::getGender);
            }
        }

        IPage<Person> pages = mapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);

        List<PersonVOBeta> persons = VOMapper.toBetaVO(pages.getRecords());

        return new SearchResult<>(persons, pages.getTotal(), pages.getCurrent(), pages.getSize());
    }

    /**
     * 搜索person
     *
     * @param param 参数
     * @author rakbow
     */
    public SearchResult<PersonMiniVO> searchPersons(SimpleSearchParam param) {

        LambdaQueryWrapper<Person> wrapper = new LambdaQueryWrapper<Person>()
                .and(i -> i.apply("JSON_UNQUOTE(JSON_EXTRACT(aliases, '$[*]')) LIKE concat('%', {0}, '%')", param.getKeyword()))
                .or().like(Person::getName, param.getKeyword())
                .or().like(Person::getNameZh, param.getKeyword())
                .or().like(Person::getNameEn, param.getKeyword())
                .eq(Person::getStatus, 1)
                .orderByDesc(Person::getId);

        IPage<Person> pages = mapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);

        List<PersonMiniVO> persons = VOMapper.toMiniVO(pages.getRecords());

        return new SearchResult<>(persons, pages.getTotal(), pages.getCurrent(), pages.getSize());
    }
    //endregion

    //region person role

    public void addRole(PersonRole role) {
        roleMapper.insert(role);
    }

    public void updateRole(PersonRole role) {
        roleMapper.updateById(role);
    }

    public SearchResult<PersonRole> getRoles(QueryParams param) {
        String name = param.getStr("name");
        String nameZh = param.getStr("nameZh");
        String nameEn = param.getStr("nameEn");

        LambdaQueryWrapper<PersonRole> wrapper = new LambdaQueryWrapper<PersonRole>()
                .like(StringUtils.isNotBlank(name), PersonRole::getName, name)
                .like(StringUtils.isNotBlank(nameZh), PersonRole::getNameZh, nameZh)
                .like(StringUtils.isNotBlank(nameEn), PersonRole::getNameEn, nameEn);
        if (StringUtils.isNotBlank(param.sortField)) {
            switch (param.sortField) {
                case "name" -> wrapper.orderBy(true, param.asc(), PersonRole::getName);
                case "nameZh" -> wrapper.orderBy(true, param.asc(), PersonRole::getNameZh);
                case "nameEn" -> wrapper.orderBy(true, param.asc(), PersonRole::getNameEn);
            }
        } else {
            wrapper.orderByDesc(PersonRole::getId);
        }

        IPage<PersonRole> pages = roleMapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);

        return new SearchResult<>(pages);
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
                pair.setMain(r.mainFlag());
                pair.setRole(role);
                pair.setPerson(new Attribute<>(person.getName() + SLASH_WITH_SPACE + person.getNameZh(), person.getId()));
                res.addEditPersonnel(pair);

                Attribute<Long> p = new Attribute<>(person.getName(), person.getId());
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
        if(!addRelationSet.isEmpty())
            batchInsert.execute(method.insert());
        if(!deleteRelationSet.isEmpty())
            batchDelete.execute(method.deleteById());

    }

    //endregion
}
