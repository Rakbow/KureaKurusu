package com.rakbow.kureakurusu.data.emun.entity.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ReleaseType {

    UNKNOWN(0, "Unknown", "未知"),
    OFFICIAL_RELEASE(1, "Official Release", "官方发行");

    @Getter
    private final int id;
    @Getter
    private final String nameEn;
    @Getter
    private final String nameZh;

}
