package com.rakbow.kureakurusu.data.entity.item;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Rakbow
 * @since 2024/4/8 18:17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName(value = "item_album", autoResultMap = true)
public class ItemAlbum extends SubItem {

    private Long id;
    private int discs;
    private int tracks;
    private int runTime;

}
