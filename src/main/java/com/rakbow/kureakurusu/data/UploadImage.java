package com.rakbow.kureakurusu.data;

import lombok.Data;

/**
 * @author Rakbow
 * @since 2025/6/12 3:13
 */
@Data
public class UploadImage {
    private String prefixKey;
    private byte[] data;
    private String extension;
    private long size;
}
