package com.rakbow.kureakurusu.data.emun.entity.book;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2022-12-29 21:29
 * @Description:
 */
@AllArgsConstructor
public enum BookType {

    UNCATEGORIZED(0,"未分类", "Uncategorized"),
    NOVEL(1,"小说", "Novel"),
    COMIC(2,"漫画", "Comic"),
    ANTHOLOGY(3,"作品集", "Anthology"),
    ART_BOOK(4,"原画集/设定集", "Art Book"),
    ELECTRONIC_BOOK(5,"电子书", "E-book"),
    OTHER(6,"其他", "Other");

    @Getter
    private final int id;
    @Getter
    private final String nameZh;
    @Getter
    private final String nameEn;

}
