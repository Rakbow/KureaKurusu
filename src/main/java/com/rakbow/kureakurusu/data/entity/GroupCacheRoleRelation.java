package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2025/6/29 18:20
 */
@Data
@TableName(value = "group_cache_role_relation", autoResultMap = true)
public class GroupCacheRoleRelation {

    private Long roleId;
    private int count;

    //todo auto job

}
