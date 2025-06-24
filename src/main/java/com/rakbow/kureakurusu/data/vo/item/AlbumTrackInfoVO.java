package com.rakbow.kureakurusu.data.vo.item;

import com.rakbow.kureakurusu.toolkit.DateHelper;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2024/01/08 17:00
 */
@Data
public class AlbumTrackInfoVO {

    private List<AlbumDiscVO> discs;//碟片列表
    private String totalDuration;//总时长 hh:mm:ss
    private int totalTracks;//总音轨数目

    public AlbumTrackInfoVO() {
        discs = new ArrayList<>();
        totalDuration = DateHelper.EMPTY_DURATION;
        totalTracks = 0;
    }

}
