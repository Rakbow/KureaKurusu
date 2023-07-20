package com.rakbow.kureakurusu.data.bo;

import com.alibaba.fastjson2.JSONArray;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-03-28 17:11
 * @Description: 专辑-音轨增删改-业务对象
 */

@Data
public class AlbumDiscBO {

    private int serial;
    private JSONArray trackList;
    private List<Integer> mediaFormat;
    private List<Integer> albumFormat;

    public AlbumDiscBO() {
        serial = 0;
        trackList = new JSONArray();
        mediaFormat = new ArrayList<>();
        albumFormat = new ArrayList<>();
    }

}
