package com.rakbow.kureakurusu.data;

import lombok.Data;

/**
 * @author Rakbow
 * @since 2024/5/9 11:04
 */
@Data
public class ImageConfigValue {

    private int coverSize;
    private boolean isWidth;
    private String defaultUrl;

    public ImageConfigValue(int coverSize, boolean isWidth) {
        this.coverSize = coverSize;
        this.isWidth = isWidth;
        this.defaultUrl = CommonConstant.EMPTY_IMAGE_URL;
    }

    public ImageConfigValue(int coverSize, boolean isWidth, String defaultUrl) {
        this.coverSize = coverSize;
        this.isWidth = isWidth;
        this.defaultUrl = defaultUrl;
    }

}
