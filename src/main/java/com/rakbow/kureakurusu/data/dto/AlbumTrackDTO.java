package com.rakbow.kureakurusu.data.dto;

import lombok.Data;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-03-28 16:14
 * @Description: 专辑-音轨增删改-中间对象
 */
@Data
public class AlbumTrackDTO {

    private int musicId;
    private String name;
    private String length;
    private String serial;

    public AlbumTrackDTO() {
        musicId = 0;
        name = "";
        length = "00:00";
        serial = "01";
    }

}
