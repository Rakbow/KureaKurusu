package com.rakbow.kureakurusu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rakbow.kureakurusu.data.entity.Book;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Rakbow
 * @since 2022-12-28 22:19
 */
@Mapper
public interface BookMapper extends BaseMapper<Book> {
}
