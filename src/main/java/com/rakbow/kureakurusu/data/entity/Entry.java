package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rakbow.kureakurusu.data.emun.EntryType;
import com.rakbow.kureakurusu.data.entity.common.MetaEntity;
import com.rakbow.kureakurusu.data.vo.entry.EntryVO;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.handler.StrListHandler;
import com.rakbow.kureakurusu.toolkit.jackson.BooleanToIntDeserializer;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.github.linpeilie.annotations.AutoMapping;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Rakbow
 * @since 2024/6/18 18:12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName(value = "entry", autoResultMap = true)
@AutoMappers({
        @AutoMapper(target = EntryVO.class, reverseConvertGenerate = false)
})
public class Entry extends MetaEntity {

    @TableId(type = IdType.AUTO)
    private Long id;
    @AutoMapping(qualifiedByName = "toAttribute")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private EntryType type;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long mfcEntryId;// myFigureCollection entry id
    private String name;
    private String nameEn;
    private String nameZh;
    @TableField(typeHandler = StrListHandler.class)
    private List<String> aliases;
    private String date;// event-date
    @TableField(typeHandler = StrListHandler.class)
    private List<String> links;
    private String detail;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String cover;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String image;

    @TableField(updateStrategy = FieldStrategy.NEVER)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateHelper.DATE_TIME_FORMAT, timezone="GMT+8")
    private Timestamp addedTime = DateHelper.now();
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateHelper.DATE_TIME_FORMAT, timezone="GMT+8")
    private Timestamp editedTime = DateHelper.now();
    @JsonDeserialize(using = BooleanToIntDeserializer.class)
    private Boolean status;

}
