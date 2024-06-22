package com.rakbow.kureakurusu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rakbow.kureakurusu.data.entity.Role;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Rakbow
 * @since 2023-12-10 6:33
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
}
