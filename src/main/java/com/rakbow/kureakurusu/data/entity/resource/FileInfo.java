package com.rakbow.kureakurusu.data.entity.resource;

import com.baomidou.mybatisplus.annotation.*;
import com.rakbow.kureakurusu.data.entity.Entity;
import com.rakbow.kureakurusu.data.vo.episode.EpisodeListVO;
import com.rakbow.kureakurusu.data.vo.episode.EpisodeVO;
import com.rakbow.kureakurusu.data.vo.resource.FileListVO;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.github.linpeilie.annotations.AutoMapping;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.File;

/**
 * @author Rakbow
 * @since 2025/5/29 16:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@TableName(value = "file_info", autoResultMap = true)
@AutoMappers({
        @AutoMapper(target = FileListVO.class, reverseConvertGenerate = false)
})
public class FileInfo extends Entity {

    @TableId(type = IdType.AUTO)
    private Long id = 0L;
    @AutoMapping(qualifiedByName = "getFileExt", target = "ext")
    private String name;

    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String mime;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @AutoMapping(qualifiedByName = "size")
    private long size;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String path;

    @TableField(exist = false)
    private File file;

}
