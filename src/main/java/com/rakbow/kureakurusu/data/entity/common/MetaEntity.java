package com.rakbow.kureakurusu.data.entity.common;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.handler.StrListHandler;
import com.rakbow.kureakurusu.toolkit.jackson.BooleanToIntDeserializer;
import io.github.linpeilie.annotations.AutoMapping;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Rakbow
 * @since 2023-05-02 3:55
 */
@Data
public class MetaEntity {

    private Long id;
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String name;
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String nameZh;
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String nameEn;
    @TableField(typeHandler = StrListHandler.class)
    private List<String> aliases;

    private String detail;
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

    public void updateEditedTime() {
        this.editedTime = DateHelper.now();
    }

}
