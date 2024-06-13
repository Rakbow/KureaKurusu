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
import com.rakbow.kureakurusu.data.dto.PersonDetailQry;
import com.rakbow.kureakurusu.data.dto.PersonListParams;
import com.rakbow.kureakurusu.data.dto.PersonnelCreateDTO;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.entity.Person;
import com.rakbow.kureakurusu.data.entity.PersonRelation;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.data.person.Personnel;
import com.rakbow.kureakurusu.data.vo.person.PersonDetailVO;
import com.rakbow.kureakurusu.data.vo.person.PersonVO;
import com.rakbow.kureakurusu.data.vo.person.PersonVOBeta;
import com.rakbow.kureakurusu.toolkit.CommonUtil;
import com.rakbow.kureakurusu.toolkit.DataFinder;
import com.rakbow.kureakurusu.toolkit.EntityUtil;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import com.rakbow.kureakurusu.toolkit.convert.PersonVOMapper;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    private final Converter converter;
    private final int ENTITY_VALUE = EntityType.PERSON.getValue();

    @SneakyThrows
    @Transactional
    public PersonDetailVO detail(PersonDetailQry qry) {
        Person person = getById(qry.getId());
        if (person == null)
            throw new Exception(I18nHelper.getMessage("entity.url.error", EntityType.PERSON.getLabel()));

        return PersonDetailVO.builder()
                .item(converter.convert(person, PersonVO.class))
                .traffic(entityUtil.getPageTraffic(ENTITY_VALUE, qry.getId()))
                .options(entityUtil.getDetailOptions(ENTITY_VALUE))
                .build();
    }

    @Transactional
    public SearchResult<PersonVOBeta> getPersons(PersonListParams param) {

        QueryWrapper<Person> wrapper = new QueryWrapper<Person>()
                .like(StringUtils.isNotBlank(param.getAliases()),
                        "JSON_UNQUOTE(JSON_EXTRACT(aliases, '$[*]'))", STR."%\{param.getAliases()}%")
                .like("name", param.getName())
                .like("name_zh", param.getNameZh())
                .like("name_en", param.getNameEn())
                .in(CollectionUtils.isNotEmpty(param.getGender()), "gender", param.getGender())
                .orderBy(param.isSort(), param.asc(), CommonUtil.camelToUnderline(param.sortField));

        IPage<Person> pages = mapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);
        List<PersonVOBeta> persons = VOMapper.toBetaVO(pages.getRecords());
        return new SearchResult<>(persons, pages.getTotal(), pages.getCurrent(), pages.getSize());
    }
    //endregion

    //region relation

    @Transactional
    public List<Personnel> getPersonnel(int entityType, long entityId) {
        List<Personnel> res = new ArrayList<>();
        if (MetaData.optionsZh.roleSet.isEmpty())
            return res;
        List<PersonRelation> relations = relationMapper.selectList(
                new LambdaQueryWrapper<PersonRelation>()
                        .eq(PersonRelation::getEntityType, entityType)
                        .eq(PersonRelation::getEntityId, entityId)
                        .orderByAsc(PersonRelation::getRoleId)
        );
        if (relations.isEmpty())
            return res;
        List<Long> personIds = relations.stream().map(PersonRelation::getPersonId).distinct().toList();
        List<Person> persons = mapper.selectBatchIds(personIds);

        for (PersonRelation r : relations) {
            Person person = DataFinder.findPersonById(r.getPersonId(), persons);
            if (person == null) continue;
            Attribute<Long> role = DataFinder.findAttributeByValue(r.getRoleId(), MetaData.optionsZh.roleSet);
            if (role == null) continue;
            res.add(
                    Personnel.builder()
                            .id(r.getId())
                            .role(role)
                            .person(new Attribute<>(person.getName(), person.getId()))
                            .build()
            );
        }
        return res;
    }

    @Transactional
    public void addPersonnel(PersonnelCreateDTO dto) {
        List<PersonRelation> res = new ArrayList<>();
        for (Long personId : dto.getPersonIds()) {
            PersonRelation personnel = new PersonRelation();
            personnel.setEntityType(dto.getEntityType());
            personnel.setEntityId(dto.getEntityId());
            personnel.setRoleId(dto.getRoleId());
            personnel.setPersonId(personId);
            res.add(personnel);
        }
        //batch insert
        MybatisBatch.Method<PersonRelation> method = new MybatisBatch.Method<>(PersonRelationMapper.class);
        MybatisBatch<PersonRelation> batchInsert = new MybatisBatch<>(sqlSessionFactory, res);
        batchInsert.execute(method.insert());
    }

    //endregion
}
