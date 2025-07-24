package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author Rakbow
 * @since 2025/7/24 19:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "fav_list", autoResultMap = true)
@NoArgsConstructor
public class FavList extends Entity {

    private Long id;

    @TableField(updateStrategy = FieldStrategy.NEVER)
    private int type;

    @NotBlank(message = "{entity.crud.name.required_field}")
    private String name;

    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String creator;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateHelper.DATE_TIME_FORMAT, timezone="GMT+8")
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    @OrderBy
    private Timestamp createTime;

}
