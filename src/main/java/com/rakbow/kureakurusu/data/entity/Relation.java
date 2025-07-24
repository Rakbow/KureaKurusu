package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rakbow
 * @since 2024/6/22 15:53
 */
@Data
@Builder
@TableName(value = "relation", autoResultMap = true)
@NoArgsConstructor
@AllArgsConstructor
public class Relation {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long roleId;
    private Long relatedRoleId;

    private int entityType;
    private int entitySubType;
    private Long entityId;

    private int relatedEntityType;
    private int relatedEntitySubType;
    private Long relatedEntityId;

    private String remark;

    @TableField(exist = false)
    private int direction = 1;

}
