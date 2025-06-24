package com.rakbow.kureakurusu.data.vo.item;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2024/01/08 17:34
 */
@Data
public class AlbumDiscVO {

    private long id;
    private long itemId;
    private String catalogId;
    private int discNo;
    private Attribute<Integer> mediaFormat;
    private List<Attribute<Integer>> albumFormat;

    private List<AlbumTrackVO> tracks;//音轨列表
    private String duration;//碟片时长 hh:mm:ss

    private String addedTime;
    private String editedTime;
    private boolean status;

    public AlbumDiscVO() {
        discNo = 1;
        tracks = new ArrayList<>();
        duration = DateHelper.EMPTY_DURATION;
    }

}
