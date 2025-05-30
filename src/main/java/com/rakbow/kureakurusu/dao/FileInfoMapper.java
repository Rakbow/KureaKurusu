package com.rakbow.kureakurusu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rakbow.kureakurusu.data.entity.resource.FileInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Rakbow
 * @since 2025/5/29 17:52
 */
@Mapper
public interface FileInfoMapper extends BaseMapper<FileInfo> {
}
