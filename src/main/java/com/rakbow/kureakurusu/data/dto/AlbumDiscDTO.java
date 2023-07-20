package com.rakbow.kureakurusu.data.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-03-28 16:11
 * @Description: 专辑-音轨增删改-中间对象
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
