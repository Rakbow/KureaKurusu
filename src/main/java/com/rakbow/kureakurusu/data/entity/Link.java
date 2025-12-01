package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.enums.EntityType;
import com.rakbow.kureakurusu.data.enums.LinkType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Rakbow
 * @since 2023-10-06 4:14
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@TableName(value = "link", autoResultMap = true)
public class Link extends Entity {

    private LinkType type;
    private EntityType entityType;
    private Long entityId;
    private String title;
    private String url;

    @TableLogic
    private int del;

}