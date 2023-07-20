package com.rakbow.kureakurusu.data.emun.image;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2022-11-18 23:01
 * @Description:
 */
@AllArgsConstructor
public enum ImageType {
    DISPLAY(0,"展示","Display"),
    COVER(1, "主要","Main"),
    OTHER(2, "其他","Other");

    @Getter
    private final int id;
    @Getter
    private final String nameZh;
    @Getter
    private final String nameEn;

}
