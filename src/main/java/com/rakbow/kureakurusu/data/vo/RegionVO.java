package com.rakbow.kureakurusu.data.vo;

import lombok.Data;

/**
 * @author Rakbow
 * @since 2023-05-16 23:00
 */
@Data
public class RegionVO {

    private String code;
    private String name;

    public RegionVO(String code, String name) {
        this.code = code;
        this.name = name;
    }

}
