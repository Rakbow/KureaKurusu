package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.emun.RoleGroup;
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

    private Long id;
    private RoleGroup roleGroup;
    private Long roleId;
    private int entityType;
    private Long entityId;
    private int relatedEntityType;
    private Long relatedEntityId;
    private String remark;
    private Boolean status;

}
