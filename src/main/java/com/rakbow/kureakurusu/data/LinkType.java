package com.rakbow.kureakurusu.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum LinkType {
    OFFICIAL(0, "enum.link_type.official"),
    BLOG(1, "enum.link_type.blog"),
    TWITTER(2, "enum.link_type.twitter"),
    FACEBOOK(3, "enum.link_type.facebook"),
    FanClub(4, "enum.link_type.fan_club"),
    OTHER(99, "enum.link_type.other");

    @Getter
    private final int value;
    @Getter
    private final String labelKey;
}
