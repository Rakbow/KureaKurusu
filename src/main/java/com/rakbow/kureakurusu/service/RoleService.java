package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.RoleMapper;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.RoleListQueryDTO;
import com.rakbow.kureakurusu.data.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Rakbow
 * @since 2024/3/18 0:26
 */
@Service
@RequiredArgsConstructor
public class RoleService extends ServiceImpl<RoleMapper, Role> {

    @Transactional
    public SearchResult<Role> getRoles(RoleListQueryDTO param) {

        QueryWrapper<Role> wrapper = new QueryWrapper<Role>()
                .like("name", param.getName())
                .like("name_zh", param.getNameZh())
                .like("name_en", param.getNameEn())
                .orderBy(param.isSort(), param.asc(), param.getSortField());

        IPage<Role> pages = page(new Page<>(param.getPage(), param.getSize()), wrapper);
        return new SearchResult<>(pages);
    }

}
