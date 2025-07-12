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
import com.rakbow.kureakurusu.data.entity.GroupCacheRoleRelation;
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
    public SearchResult<Role> list(RoleListQueryDTO dto) {
        dto.init();
        long start = System.currentTimeMillis();
        MPJLambdaWrapper<Role> wrapper = new MPJLambdaWrapper<Role>()
                .selectAll(Role.class)
                .select(GroupCacheRoleRelation::getCount)
                .leftJoin(GroupCacheRoleRelation.class,
                        on -> on.eq(GroupCacheRoleRelation::getRoleId, Role::getId)
                )
                .groupBy(Role::getId)
                .and(StringUtils.isNotEmpty(dto.getKeyword()), i -> i
                        .or().like(Role::getName, dto.getKeyword())
                        .or().like(Role::getNameZh, dto.getKeyword())
                        .or().like(Role::getNameEn, dto.getKeyword())
                )
                .gt(Role::getId, 0)
                .orderBy(dto.isSort(), dto.asc(), CommonUtil.camelToUnderline(dto.getSortField()))
                .orderByDesc(!dto.isSort(), GroupCacheRoleRelation::getCount);

        IPage<Role> pages = page(new Page<>(dto.getPage(), dto.getSize()), wrapper);
        return new SearchResult<>(pages.getRecords(), pages.getTotal(), start);
    }

    public void refresh() {
        MetaData.optionsZh.roleSet.clear();
        MetaData.optionsEn.roleSet.clear();
        List<Role> roles = list(new LambdaQueryWrapper<Role>().orderByAsc(Role::getId));
        roles.forEach(i -> {
            MetaData.optionsZh.roleSet.add(new Attribute<>(i.getNameZh(), i.getId()));
            MetaData.optionsEn.roleSet.add(new Attribute<>(i.getNameEn(), i.getId()));
        });
        redisUtil.delete(STR."\{RedisKey.OPTION_ROLE_SET}*");
        redisUtil.set(STR."\{RedisKey.OPTION_ROLE_SET}:zh", MetaData.optionsZh.roleSet);
        redisUtil.set(STR."\{RedisKey.OPTION_ROLE_SET}:en", MetaData.optionsEn.roleSet);
    }

}
