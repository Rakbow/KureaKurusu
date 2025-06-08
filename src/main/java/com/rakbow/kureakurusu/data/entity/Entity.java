package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
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

    private String remark;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateHelper.DATE_TIME_FORMAT, timezone="GMT+8")
    @AutoMapping(qualifiedByName = "getVOTime")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Timestamp addedTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateHelper.DATE_TIME_FORMAT, timezone="GMT+8")
    @AutoMapping(qualifiedByName = "getVOTime")
    private Timestamp editedTime;
    @JsonDeserialize(using = BooleanToIntDeserializer.class)
    private Boolean status;

}
