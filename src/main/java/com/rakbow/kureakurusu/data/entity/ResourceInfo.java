package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.enums.EntityResourceType;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2025/12/14 15:59
 */
@Data
@TableName(value = "r5_resource_info", autoResultMap = true)
public class ResourceInfo {

    @TableId
    private Long id;
    private Integer entityType;
    private Long entityId;
    private EntityResourceType type;
    private String path;
    private String remark;

}
