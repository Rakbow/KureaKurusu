package com.rakbow.kureakurusu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rakbow.kureakurusu.entity.Album;
import com.rakbow.kureakurusu.entity.Person;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-10-29 16:18
 * @Description:
 */
@Mapper
public interface PersonMapper extends BaseMapper<Person> {
}
