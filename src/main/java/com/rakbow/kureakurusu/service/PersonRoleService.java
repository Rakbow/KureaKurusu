package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.PersonRoleMapper;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.PersonRoleListParams;
import com.rakbow.kureakurusu.data.entity.PersonRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Rakbow
 * @since 2024/3/18 0:26
 */
@Service
@RequiredArgsConstructor
public class PersonRoleService extends ServiceImpl<PersonRoleMapper, PersonRole> {

    private final PersonRoleMapper mapper;

    @Transactional
    public SearchResult<PersonRole> getRoles(PersonRoleListParams param) {

        QueryWrapper<PersonRole> wrapper = new QueryWrapper<PersonRole>()
                .like("name", param.getName())
                .like("name_zh", param.getNameZh())
                .like("name_en", param.getNameEn())
                .orderBy(param.isSort(), param.asc(), param.sortField);

        IPage<PersonRole> pages = mapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);
        return new SearchResult<>(pages);
    }

}
