package com.rakbow.kureakurusu.data.dto;

import lombok.Data;

/**
 * @author Rakbow
 * @since 2024/5/24 16:03
 */
@Data
public class ImageMiniDTO {

    private String base64Code;//Base64
    private String name;
    private int type;
    private String detail;

}
