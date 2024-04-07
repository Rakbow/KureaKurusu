package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Rakbow
 * @since 2024/4/7 17:29
 */
@Data
@TableName(value = "item", autoResultMap = true)
public class Item {

    private Long id;
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String name;
    private String nameZh;
    private String nameEn;

}
