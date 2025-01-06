package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.PersonMapper;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.PersonListParams;
import com.rakbow.kureakurusu.data.entity.entry.Person;
import com.rakbow.kureakurusu.data.vo.person.PersonVOBeta;
import com.rakbow.kureakurusu.toolkit.CommonUtil;
import com.rakbow.kureakurusu.toolkit.convert.PersonVOMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
