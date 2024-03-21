package com.rakbow.kureakurusu.data.emun;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2022-11-18 23:01
 */
@AllArgsConstructor
@Getter
public enum ImageType {

    DISPLAY(0),
    MAIN(1),
    OTHER(2);

    private final Integer value;

}
