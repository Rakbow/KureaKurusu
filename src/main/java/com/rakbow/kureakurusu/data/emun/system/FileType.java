package com.rakbow.kureakurusu.data.emun.system;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2023-02-02 15:54
 */
@AllArgsConstructor
public enum FileType {
    UNCATEGORIZED(0,"未分类", "Uncategorized"),
    IMAGE(1, "图片", "Image"),
    AUDIO(2, "音频", "Audio"),
    VIDEO(3, "视频", "Video"),
    TEXT(4, "文本", "Text"),
    OTHER(5, "其他", "Other");

    @Getter
    private final int index;
    @Getter
    private final String nameZh;
    @Getter
    private final String nameEn;
}
