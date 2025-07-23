package com.rakbow.kureakurusu.data.entity.resource;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.rakbow.kureakurusu.data.entity.Entity;
import com.rakbow.kureakurusu.data.vo.resource.FileListVO;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.convert.GlobalConverters;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.github.linpeilie.annotations.AutoMapping;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.File;
import java.sql.Timestamp;

/**
 * @author Rakbow
 * @since 2025/5/29 16:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@TableName(value = "file_info", autoResultMap = true)
@AutoMappers({
        @AutoMapper(target = FileListVO.class, reverseConvertGenerate = false, uses = GlobalConverters.class)
})
public class FileInfo extends Entity {

    @TableId(type = IdType.AUTO)
    private Long id = 0L;
    private String name;

    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String extension;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @AutoMapping(qualifiedByName = "size")
    private long size;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String path;

    @TableField(exist = false)
    private File file;
    @TableField(exist = false)
    private long relatedId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateHelper.DATE_TIME_FORMAT, timezone="GMT+8")
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private Timestamp addedTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateHelper.DATE_TIME_FORMAT, timezone="GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Timestamp editedTime;

}
