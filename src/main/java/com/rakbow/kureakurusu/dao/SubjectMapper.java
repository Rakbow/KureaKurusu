package com.rakbow.kureakurusu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rakbow.kureakurusu.data.entity.entry.Subject;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Rakbow
 * @since 2024/7/2 21:37
 */
@Mapper
public interface SubjectMapper extends BaseMapper<Subject> {
}
