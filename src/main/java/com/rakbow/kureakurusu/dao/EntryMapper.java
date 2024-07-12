package com.rakbow.kureakurusu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rakbow.kureakurusu.data.entity.Entry;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Rakbow
 * @since 2024/7/2 21:37
 */
@Mapper
public interface EntryMapper extends BaseMapper<Entry> {
}
