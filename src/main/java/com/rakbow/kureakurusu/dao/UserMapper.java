package com.rakbow.kureakurusu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rakbow.kureakurusu.data.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Rakbow
 * @since 2022-08-02 0:46
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
