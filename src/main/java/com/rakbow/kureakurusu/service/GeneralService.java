package com.rakbow.kureakurusu.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rakbow.kureakurusu.dao.CommonMapper;
import com.rakbow.kureakurusu.dao.PersonMapper;
import com.rakbow.kureakurusu.data.*;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.emun.temp.EnumUtil;
import com.rakbow.kureakurusu.data.vo.person.PersonMiniVO;
import com.rakbow.kureakurusu.data.vo.person.PersonVOBeta;
import com.rakbow.kureakurusu.entity.Person;
import com.rakbow.kureakurusu.util.EnumHelper;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.RedisUtil;
import com.rakbow.kureakurusu.util.convertMapper.entity.PersonVOMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-05-19 18:56
 * @Description:
 */
@Service
public class GeneralService {

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private CommonMapper commonMapper;
    @Resource
    private PersonMapper personMapper;

    private static final Logger logger = LoggerFactory.getLogger(GeneralService.class);

    private final PersonVOMapper personVOMapper = PersonVOMapper.INSTANCES;

    /**
     * 刷新redis中的选项缓存
     *
     * @author rakbow
     */
    public void refreshRedisEnumData() {

        Map<String, List<Attribute<Integer>>> enumOptionsRedisKeyPair = EnumUtil.getOptionRedisKeyPair();
        enumOptionsRedisKeyPair.forEach((k, v) -> {
            redisUtil.set(k, v);
        });

    }

    public void loadMetaData() {

        MetaData.optionsZh = new MetaOption();
        MetaData.optionsEn = new MetaOption();
        MetaData.optionsZh.genderSet = EnumHelper.getAttributeOptions(Gender.class, "zh");
        MetaData.optionsEn.genderSet = EnumHelper.getAttributeOptions(Gender.class, "en");
        MetaData.optionsZh.linkTypeSet = EnumHelper.getAttributeOptions(LinkType.class, "zh");
        MetaData.optionsEn.linkTypeSet = EnumHelper.getAttributeOptions(LinkType.class, "en");

        logger.info(I18nHelper.getMessage("system.load_data.meta_data"));

        logger.info(JSON.toJSONString(MetaData.optionsZh));
        logger.info(JSON.toJSONString(MetaData.optionsEn));

    }

    //region person

    public void addPerson(Person person) {
        personMapper.insert(person);
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
                .set(Person::getRemark, person.getRemark());

        personMapper.update(null, wrapper);
    }

    public void deletePerson(Person person) {
        personMapper.deleteById(person);
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

        IPage<Person> pages = personMapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);

        List<PersonVOBeta> persons = personVOMapper.toBetaVO(pages.getRecords());

        return new SearchResult(persons, pages);
    }

    /**
     * 搜索person
     *
     * @author rakbow
     * @param param 参数
     */
    public SearchResult searchPersons(SimpleSearchParam param) {

        LambdaQueryWrapper<Person> wrapper = new LambdaQueryWrapper<Person>()
                .and(i -> i.apply("JSON_UNQUOTE(JSON_EXTRACT(aliases, '$[*]')) LIKE concat('%', {0}, '%')", param.getKeyword()))
                .or().like(Person::getName, param.getKeyword())
                .or().like(Person::getNameZh, param.getKeyword())
                .or().like(Person::getNameEn, param.getKeyword())
                .eq(Person::getStatus, 1)
                .orderByDesc(Person::getId);

        IPage<Person> pages = personMapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);

        List<PersonMiniVO> persons = personVOMapper.toMiniVO(pages.getRecords());

           return new SearchResult(persons, pages);
    }

    //endregion

}
