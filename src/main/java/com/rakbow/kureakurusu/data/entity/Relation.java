package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.emun.RelatedGroup;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rakbow
 * @since 2024/6/22 15:53
 */
@Data
@TableName(value = "relation", autoResultMap = true)
@NoArgsConstructor

public class Relation {

    @TableId(type = IdType.AUTO)
    private Long id;
    private RelatedGroup relatedGroup;
    private Long roleId;
    private Long reverseRoleId;
    private int entityType;
    private Long entityId;
    private int relatedEntityType;
    private Long relatedEntityId;
    private String remark;
    private Boolean status;

    @TableField(exist = false)
    private int direction = 1;

}
