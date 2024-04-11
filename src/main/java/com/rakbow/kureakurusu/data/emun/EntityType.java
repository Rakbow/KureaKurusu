package com.rakbow.kureakurusu.data.emun;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2024/4/12 2:23
 */
@Getter
@AllArgsConstructor
public enum EntityType {

    ITEM(0),
    SUBJECT(1),
    PERSON(2),
    ROLE(3),
    EPISODE(4);

    private final Integer value;
}
