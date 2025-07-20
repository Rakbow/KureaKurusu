package com.rakbow.kureakurusu.data.dto;

import lombok.Data;

/**
 * @author Rakbow
 * @since 2025/7/20 15:15
 */
@Data
public class AlbumTrackQuickUploadDTO {

    private long id;
    private long itemId;
    private String albumCatalogId;
    private String discCatalogId;
    private int discNo;

}
