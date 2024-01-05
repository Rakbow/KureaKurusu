package com.rakbow.kureakurusu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rakbow.kureakurusu.data.entity.Person;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Rakbow
 * @since 2023-10-29 16:18
 */
@Mapper
public interface PersonMapper extends BaseMapper<Person> {
}
