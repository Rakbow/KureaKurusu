package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author Rakbow
 * @since 2025/7/24 22:11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "r4_index_element", autoResultMap = true)
@NoArgsConstructor
public class IndexElement extends Entity {

    @TableId(type = IdType.AUTO)
    private Long id;
    private long indexId;
    private int entityType;
    private long entityId;
    private Timestamp createdAt;

    public IndexElement(long indexId, int entityType, long entityId) {
        this.indexId = indexId;
        this.entityType = entityType;
        this.entityId = entityId;
        this.createdAt = DateHelper.now();
    }

}
