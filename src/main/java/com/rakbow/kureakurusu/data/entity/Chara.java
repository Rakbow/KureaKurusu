package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rakbow.kureakurusu.data.emun.Gender;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.handler.StrListHandler;
import com.rakbow.kureakurusu.toolkit.jackson.BooleanToIntDeserializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Rakbow
 * @since 2024/6/22 12:44
 */
@Data
@TableName(value = "character", autoResultMap = true)
@NoArgsConstructor
public class Chara {

    private Long id;
    private String name;
    private String nameEn;
    @TableField(typeHandler = StrListHandler.class)
    private List<String> aliases;
    private Gender gender;
    private String birthday;
    private String detail;
    @TableField(typeHandler = StrListHandler.class)
    private List<String> links;

    private String favorites;
    private String hates;
    private String bloodGroup;

    private String remark;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateHelper.DATE_TIME_FORMAT, timezone="GMT+8")
    private Timestamp addedTime = DateHelper.now();
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateHelper.DATE_TIME_FORMAT, timezone="GMT+8")
    private Timestamp editedTime = DateHelper.now();
    @JsonDeserialize(using = BooleanToIntDeserializer.class)
    private Boolean status;

}
