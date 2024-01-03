package com.rakbow.kureakurusu.data.vo;

import lombok.Data;

/**
 * @author Rakbow
 * @since 2023-05-16 22:59
 */
@Data
public class LanguageVO {

    private String code;
    private String name;

    public LanguageVO(String code, String name) {
        this.code = code;
        this.name = name;
    }

}
