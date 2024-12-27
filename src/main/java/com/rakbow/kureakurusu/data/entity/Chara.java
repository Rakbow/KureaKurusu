package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rakbow.kureakurusu.data.emun.Gender;
import com.rakbow.kureakurusu.data.entity.common.MetaEntity;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.handler.StrListHandler;
import com.rakbow.kureakurusu.toolkit.jackson.BooleanToIntDeserializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Rakbow
 * @since 2024/6/22 12:44
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "chara", autoResultMap = true)
@NoArgsConstructor
public class Chara extends MetaEntity {

    private Long id;
    private String name;
    private String nameZh;
    private String nameEn;
    @TableField(typeHandler = StrListHandler.class)
    private List<String> aliases;
    private Gender gender;
    private String birthday;
    @TableField(typeHandler = StrListHandler.class)
    private List<String> links;

    // private String favorites;
    // private String hates;
    // private String bloodGroup;

}
