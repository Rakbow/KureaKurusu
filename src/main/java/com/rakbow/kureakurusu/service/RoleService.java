package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.rakbow.kureakurusu.dao.RoleMapper;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.RedisKey;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.RoleListQueryDTO;
import com.rakbow.kureakurusu.data.entity.Relation;
import com.rakbow.kureakurusu.data.entity.Role;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.toolkit.CommonUtil;
import com.rakbow.kureakurusu.toolkit.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/3/18 0:26
 */
@Service
@RequiredArgsConstructor
public class RoleService extends ServiceImpl<RoleMapper, Role> {

    private final RedisUtil redisUtil;

    @Transactional
    public SearchResult<Role> list(RoleListQueryDTO param) {

        MPJLambdaWrapper<Role> wrapper = new MPJLambdaWrapper<Role>()
                .selectAll(Role.class)
                .selectCount(Relation::getId, "citations")
                .leftJoin(Relation.class,
                        on -> on.eq(Relation::getRoleId, Role::getId)
                )
                .groupBy(Role::getId)
                .and(StringUtils.isNotEmpty(param.getKeyword()), i -> i
                        .or().like(Role::getName, param.getKeyword())
                        .or().like(Role::getNameZh, param.getKeyword())
                        .or().like(Role::getNameEn, param.getKeyword())
                )
                .orderBy(param.isSort(), param.asc(), CommonUtil.camelToUnderline(param.getSortField()))
                .orderByDesc(!param.isSort(), "citations");

        IPage<Role> pages = page(new Page<>(param.getPage(), param.getSize()), wrapper);
        return new SearchResult<>(pages);
    }

    public void refresh() {
        MetaData.optionsZh.roleSet.clear();
        MetaData.optionsEn.roleSet.clear();
        List<Role> roles = list();
        roles.forEach(i -> {
            MetaData.optionsZh.roleSet.add(new Attribute<>(i.getNameZh(), i.getId()));
            MetaData.optionsEn.roleSet.add(new Attribute<>(i.getNameEn(), i.getId()));
        });
        redisUtil.delete(STR."\{RedisKey.OPTION_ROLE_SET}*");
        redisUtil.set(STR."\{RedisKey.OPTION_ROLE_SET}:zh", MetaData.optionsZh.roleSet);
        redisUtil.set(STR."\{RedisKey.OPTION_ROLE_SET}:en", MetaData.optionsEn.roleSet);
    }

}
