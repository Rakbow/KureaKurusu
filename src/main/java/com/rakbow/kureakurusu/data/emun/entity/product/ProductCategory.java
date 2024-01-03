package com.rakbow.kureakurusu.data.emun.entity.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2022-08-20 2:54
 */
@AllArgsConstructor
public enum ProductCategory {
    UNCATEGORIZED(0,"未分类", "Uncategorized"),
    GAME(1, "游戏", "Game"),
    ANIMATION(2, "TV动画/动画电影", "Animation"),
    OVA_OAD(3, "OVA/OAD", "OVA/OAD"),

    NOVEL(4, "小说", "Novel"),
    MANGA(5, "漫画", "Manga"),
    PUBLICATION(6, "其他出版物", "Publication"),
    LIVE_ACTION_MOVIE(7, "真人电影", "Live Action Movie"),
    TV_SERIES(8, "电视剧", "TV Series"),
    MISC(99, "杂项", "Misc");

    @Getter
    private final int id;
    @Getter
    private final String nameZh;
    @Getter
    private final String nameEn;

}
