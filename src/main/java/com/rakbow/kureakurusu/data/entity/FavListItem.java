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
@TableName(value = "fav_list_item", autoResultMap = true)
@NoArgsConstructor
public class FavListItem extends Entity {

    @TableId(type = IdType.AUTO)
    private Long id;
    private long listId;
    private int entityType;
    private long entityId;
    private Timestamp createdAt;

    public FavListItem(long listId, int entityType, long entityId) {
        this.listId = listId;
        this.entityType = entityType;
        this.entityId = entityId;
        this.createdAt = DateHelper.now();
    }

}
