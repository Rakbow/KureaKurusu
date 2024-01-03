package com.rakbow.kureakurusu.data.emun.system;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2022-10-07 4:02
 */
@Getter
@AllArgsConstructor
public enum DataActionType {
    INSERT(0,"新增"),
    UPDATE(1,"更新"),
    REAL_DELETE(2,"真删"),
    FAKE_DELETE(3,"假删"),
    ALL_DELETE(4,"全删");

    private final int value;
    private final String name;
}
