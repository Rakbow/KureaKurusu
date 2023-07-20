package com.rakbow.kureakurusu.data.emun.entity.music;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2022-11-06 20:31
 * @Description:
 */
@AllArgsConstructor
public enum AudioType {
    UNCATEGORIZED(0,"未分类", "Uncategorized"),
    VOCAL(1, "歌曲","Vocal"),
    INSTRUMENTAL(2, "歌曲(无伴奏)","Instrumental"),
    ORIGINAL_SOUNDTRACK(3, "原声","Origin sound track"),
    DRAMA(4, "广播剧","Drama");

    @Getter
    private final int id;
    @Getter
    private final String nameZh;
    @Getter
    private final String nameEn;

}
