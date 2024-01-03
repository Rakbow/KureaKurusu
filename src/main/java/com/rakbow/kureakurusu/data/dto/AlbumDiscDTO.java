package com.rakbow.kureakurusu.data.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 专辑-音轨增删改-中间对象
 *
 * @author Rakbow
 * @since 2023-03-28 16:11
 */
@Data
public class AlbumDiscDTO {

    private List<AlbumTrackDTO> trackList;
    private List<String> mediaFormat;
    private List<String> albumFormat;

    public AlbumDiscDTO() {
        trackList = new ArrayList<>();
        mediaFormat = new ArrayList<>();
        albumFormat = new ArrayList<>();
    }

}
