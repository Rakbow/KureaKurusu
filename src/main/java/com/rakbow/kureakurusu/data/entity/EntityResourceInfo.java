package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2025/12/14 15:59
 */
@Data
public class EntityResourceInfo {

    @TableId
    private Long id;
    private Integer entityType;
    private Integer entitySubType;
    private Long entityId;
    private String path;

}
