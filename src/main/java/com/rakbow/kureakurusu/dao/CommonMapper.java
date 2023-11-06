package com.rakbow.kureakurusu.dao;

import com.rakbow.kureakurusu.entity.Person;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-10-06 3:50
 * @Description:
 */
@Mapper
public interface CommonMapper {

    List<Person> searchPersonByKeyword(String keyword, int first, int row);

    int searchPersonByKeywordCount(String keyword);

}
