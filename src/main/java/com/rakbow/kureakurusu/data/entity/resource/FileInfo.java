package com.rakbow.kureakurusu.data.entity.resource;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.jackson.BooleanToIntDeserializer;
import io.github.linpeilie.annotations.AutoMapping;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.sql.Timestamp;

/**
 * @author Rakbow
 * @since 2025/5/29 16:22
 */
@Data
@NoArgsConstructor
@TableName(value = "file_info", autoResultMap = true)
public class FileInfo {

    private Long id = 0L;
    private String name;
    private String mime;
    private Long size;
    private String path;
    private String remark;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateHelper.DATE_TIME_FORMAT, timezone="GMT+8")
    @AutoMapping(qualifiedByName = "getVOTime")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Timestamp uploadTime;
    @JsonDeserialize(using = BooleanToIntDeserializer.class)
    private Boolean status;

    @TableField(exist = false)
    private File file;

}
