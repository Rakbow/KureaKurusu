package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rakbow.kureakurusu.dao.CommonMapper;
import com.rakbow.kureakurusu.dao.PersonMapper;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.SimpleSearchParam;
import com.rakbow.kureakurusu.data.emun.temp.EnumUtil;
import com.rakbow.kureakurusu.data.vo.person.PersonMiniVO;
import com.rakbow.kureakurusu.entity.Person;
import com.rakbow.kureakurusu.util.EnumHelper;
import com.rakbow.kureakurusu.util.common.RedisUtil;
import com.rakbow.kureakurusu.util.convertMapper.entity.PersonVOMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Method;
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

    //region person

    /**
     * 搜索person
     *
     * @author rakbow
     * @param param 参数
     */
    public SearchResult searchPersons(SimpleSearchParam param) {

        LambdaQueryWrapper<Person> queryWrapper = new LambdaQueryWrapper<Person>()
                .and(i -> i.apply("JSON_UNQUOTE(JSON_EXTRACT(aliases, '$[*]')) LIKE concat('%', {0}, '%')", param.getKeyword()))
                .or().like(Person::getName, param.getKeyword())
                .or().like(Person::getNameZh, param.getKeyword())
                .or().like(Person::getNameEn, param.getKeyword())
                .eq(Person::getStatus, 1)
                .orderByDesc(Person::getId);

        IPage<Person> pages = personMapper.selectPage(new Page<>(param.getPage(), param.getSize()), queryWrapper);

        List<PersonMiniVO> persons = personVOMapper.toMiniVO(pages.getRecords());

           return new SearchResult(persons, pages);
    }

    //endregion

}
