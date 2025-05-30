package com.rakbow.kureakurusu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rakbow.kureakurusu.data.entity.resource.Image;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Rakbow
 * @since 2024/5/24 11:04
 */
@Mapper
public interface ImageMapper extends BaseMapper<Image> {
}
