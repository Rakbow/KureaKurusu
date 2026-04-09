package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.toolkit.jackson.BooleanToIntDeserializer;
import lombok.Data;
import tools.jackson.databind.annotation.JsonDeserialize;

/**
 * @author Rakbow
 * @since 2025/12/14 15:59
 */
@Data
@TableName(value = "r5_entity_resource_info", autoResultMap = true)
public class EntityResourceInfo {

    @TableId
    private Long id;
    private Integer entityType;
    private Integer entitySubType;
    private Long entityId;
    private String path;
    @JsonDeserialize(using = BooleanToIntDeserializer.class)
    private Boolean completedFlag;

}
