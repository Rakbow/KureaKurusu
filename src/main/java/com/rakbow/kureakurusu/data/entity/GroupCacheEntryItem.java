package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2025/6/29 21:16
 */
@Data
@TableName(value = "group_cache_entry_item", autoResultMap = true)
public class GroupCacheEntryItem {

    private Long entryId;
    private int items;

    //todo auto job

}
