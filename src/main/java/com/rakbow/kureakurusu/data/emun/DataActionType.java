package com.rakbow.kureakurusu.data.emun;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2022-10-07 4:02
 */
@Getter
@AllArgsConstructor
public enum DataActionType {
    NO_ACTION(0,"无操作"),
    INSERT(1,"新增"),
    UPDATE(2,"更新"),
    REAL_DELETE(3,"真删"),
    FAKE_DELETE(4,"假删"),
    ALL_DELETE(5,"全删");

    private final int value;
    private final String name;
}
