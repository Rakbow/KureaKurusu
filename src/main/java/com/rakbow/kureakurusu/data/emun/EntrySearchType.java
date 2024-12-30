package com.rakbow.kureakurusu.data.emun;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2024/12/28 1:38
 */
@Getter
@AllArgsConstructor
public enum EntrySearchType {

    PRODUCT(0),
    PERSON(1),
    CHARACTER(2),
    CLASSIFICATION(3),
    MATERIAL(4),
    EVENT(5);

    private final Integer value;

}
