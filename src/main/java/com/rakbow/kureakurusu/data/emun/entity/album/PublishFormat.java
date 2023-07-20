package com.rakbow.kureakurusu.data.emun.entity.album;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2022-08-19 22:34
 * @Description: 出版形式
 */
@AllArgsConstructor
public enum PublishFormat {
    UNCATEGORIZED(0,"未分类", "Uncategorized"),
    COMMERCIAL(1, "商业发行", "Commercial"),
    INDIE_DOUJIN(2,"独立同人", "Doujin"),
    BONUS(3,"同捆特典", "Bonus"),
    EVENT_ONLY(4,"展会限定", "Event Only"),
    PREORDER(5,"预约特典", "Preorder");

    @Getter
    private final int id;
    @Getter
    private final String nameZh;
    @Getter
    private final String nameEn;
}
