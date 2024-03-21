package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.PersonMapper;
import com.rakbow.kureakurusu.dao.PersonRelationMapper;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.SimpleSearchParam;
import com.rakbow.kureakurusu.data.dto.PersonDetailQry;
import com.rakbow.kureakurusu.data.dto.PersonListParams;
import com.rakbow.kureakurusu.data.dto.PersonnelManageCmd;
import com.rakbow.kureakurusu.data.emun.Entity;
import com.rakbow.kureakurusu.data.emun.DataActionType;
import com.rakbow.kureakurusu.data.entity.Person;
import com.rakbow.kureakurusu.data.entity.PersonRelation;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.data.person.Personnel;
import com.rakbow.kureakurusu.data.person.PersonnelPair;
import com.rakbow.kureakurusu.data.person.PersonnelStruct;
import com.rakbow.kureakurusu.data.vo.person.PersonDetailVO;
import com.rakbow.kureakurusu.data.vo.person.PersonMiniVO;
import com.rakbow.kureakurusu.data.vo.person.PersonVOBeta;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.ClazzHelper;
import com.rakbow.kureakurusu.util.common.DataFinder;
import com.rakbow.kureakurusu.util.common.EntityUtil;
import com.rakbow.kureakurusu.util.convertMapper.PersonVOMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.rakbow.kureakurusu.data.common.Constant.SLASH_WITH_SPACE;

/**
 * @author Rakbow
 * @since 2023-11-14 20:47
 */
@Service
@RequiredArgsConstructor
public class PersonService extends ServiceImpl<PersonMapper, Person> {

    private final PersonVOMapper VOMapper;
    private final PersonMapper mapper;
    private final PersonRelationMapper relationMapper;
    private final EntityUtil entityUtil;
    private final SqlSessionFactory sqlSessionFactory;
    private final int ENTITY_VALUE = Entity.PERSON.getValue();

    @SneakyThrows
    @Transactional
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

    @Transactional
    public SearchResult<PersonVOBeta> getPersons(PersonListParams param) {

        QueryWrapper<Person> wrapper = new QueryWrapper<Person>()
                .like("JSON_UNQUOTE(JSON_EXTRACT(aliases, '$[*]'))", STR."%\{param.getAliases()}%")
                .like("name", param.getName())
                .like("name_zh", param.getNameZh())
                .like("name_en", param.getNameEn())
                .in(CollectionUtils.isNotEmpty(param.getGender()), "gender", param.getGender())
                .orderBy(param.isSort(), param.asc(), ClazzHelper.getColumnName(param.sortField));

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
    @Transactional
    public SearchResult<PersonMiniVO> searchPersons(SimpleSearchParam param) {

        if (param.keywordEmpty()) new SearchResult<>();

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

    //region relation

    @Transactional
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
                                entry.getValue().getFirst().getId().intValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (_, b) -> b, // 如果有重复的键，保留后者
                        LinkedHashMap::new // 保持插入顺序
                ));

        for (Long roleId : sortedRelationGroup.keySet()) {
            Personnel personnel = new Personnel();
            Attribute<Long> role = DataFinder.findAttributeByValue(roleId, MetaData.optionsZh.roleSet);
            if (role == null) continue;
            personnel.setRole(role);
            List<PersonRelation> relationSet = relationGroup.get(roleId);
            for (PersonRelation r : relationSet) {
                Person person = DataFinder.findPersonById(r.getPersonId(), persons);
                if (person == null) continue;

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

    @Transactional
    public void managePersonnel(PersonnelManageCmd cmd) {

        List<PersonRelation> addRelationSet = new ArrayList<>();
        List<PersonRelation> deleteRelationSet = new ArrayList<>();

        cmd.getPersonnel().forEach(pair -> {
            if (pair.getAction() == DataActionType.INSERT.getValue())
                addRelationSet.add(new PersonRelation(pair, cmd.getEntityType(), cmd.getEntityId()));
            if (pair.getAction() == DataActionType.REAL_DELETE.getValue())
                deleteRelationSet.add(new PersonRelation(pair, cmd.getEntityType(), cmd.getEntityId()));
        });
        //批量删除和批量新增
        MybatisBatch.Method<PersonRelation> method = new MybatisBatch.Method<>(PersonRelationMapper.class);
        MybatisBatch<PersonRelation> batchInsert = new MybatisBatch<>(sqlSessionFactory, addRelationSet);
        MybatisBatch<PersonRelation> batchDelete = new MybatisBatch<>(sqlSessionFactory, deleteRelationSet);
        if (!addRelationSet.isEmpty())
            batchInsert.execute(method.insert());
        if (!deleteRelationSet.isEmpty())
            batchDelete.execute(method.deleteById());

    }

    //endregion
}
