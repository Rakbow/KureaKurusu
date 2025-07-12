package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.jackson.BooleanToIntDeserializer;
import io.github.linpeilie.annotations.AutoMapping;
import lombok.Data;

import java.sql.Timestamp;

/**
 * @author Rakbow
 * @since 2025/6/8 7:09
 */
@Data
public class Entity {

    @TableId(type = IdType.AUTO)
    private Long id = 0L;

    private String remark;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateHelper.DATE_TIME_FORMAT, timezone="GMT+8")
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private Timestamp addedTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateHelper.DATE_TIME_FORMAT, timezone="GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Timestamp editedTime;
    @JsonDeserialize(using = BooleanToIntDeserializer.class)
    private Boolean status;

}
