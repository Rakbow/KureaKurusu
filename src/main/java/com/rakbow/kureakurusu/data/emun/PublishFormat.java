package com.rakbow.kureakurusu.data.emun;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 出版形式
 *
 * @author Rakbow
 * @since 2022-08-19 22:34
 */
@Getter
@AllArgsConstructor
public enum PublishFormat {

    UNCATEGORIZED(0,"enum.publish_format.uncategorized"),
    COMMERCIAL(1, "enum.publish_format.commercial"),
    INDIE_DOUJIN(2,"enum.publish_format.indie_doujin"),
    BONUS(3,"enum.publish_format.bonus"),
    EVENT_ONLY(4,"enum.publish_format.event_only"),
    PREORDER(5,"enum.publish_format.preorder");

    @EnumValue
    private final Integer value;
    private final String labelKey;
}
