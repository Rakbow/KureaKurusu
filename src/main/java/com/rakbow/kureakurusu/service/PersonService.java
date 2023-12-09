package com.rakbow.kureakurusu.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rakbow.kureakurusu.dao.PersonMapper;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.SimpleSearchParam;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.vo.person.PersonMiniVO;
import com.rakbow.kureakurusu.data.vo.person.PersonVOBeta;
import com.rakbow.kureakurusu.entity.Person;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.convertMapper.entity.PersonVOMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-11-14 20:47
 * @Description:
 */
@Service
public class PersonService {

    private static final Logger logger = LoggerFactory.getLogger(PersonService.class);

    private final PersonVOMapper voMapper = PersonVOMapper.INSTANCES;

    @Resource
    private PersonMapper mapper;

    public Person getPerson(long id) {
        return mapper.selectById(id);
    }

    public void addPerson(Person person) {
        mapper.insert(person);
    }

    public void updatePerson(PersonVOBeta person) {

        LambdaUpdateWrapper<Person> wrapper = new LambdaUpdateWrapper<Person>()
                .eq(Person::getId, person.getId())
                .set(Person::getName, person.getName())
                .set(Person::getNameZh, person.getNameZh())
                .set(Person::getNameEn, person.getNameEn())
                .set(Person::getGender, person.getGender().getValue())
                .set(Person::getBirthDate, person.getBirthDate())
                .set(Person::getAliases, JSON.toJSONString(person.getAliases()))
                .set(Person::getRemark, person.getRemark())
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

}
