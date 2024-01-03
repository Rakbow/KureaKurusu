package com.rakbow.kureakurusu.data.emun.temp;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2023-05-22 11:02
 */
@AllArgsConstructor
public enum  OptionType {

    PERSON_ROLE(1, "人员职位", "Person Role"),
    SPEC(2, "规格参数", "Specification");

    @Getter
    private final int id;
    @Getter
    private final String nameZh;
    @Getter
    private final String nameEn;

}
