package com.rakbow.kureakurusu.data.emun;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LinkType {
    OFFICIAL(0, "enum.link_type.official"),
    BLOG(1, "enum.link_type.blog"),
    TWITTER(2, "enum.link_type.twitter"),
    FACEBOOK(3, "enum.link_type.facebook"),
    FAN_CLUB(4, "enum.link_type.fan_club"),
    OTHER(99, "enum.link_type.other");

    private final Integer value;
    private final String labelKey;

    public static LinkType get(int value) {
        for (LinkType type : LinkType.values()) {
            if(type.value == value) return type;
        }
        return OTHER;
    }

}
