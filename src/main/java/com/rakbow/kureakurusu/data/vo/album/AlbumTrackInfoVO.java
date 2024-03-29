package com.rakbow.kureakurusu.data.vo.album;

import com.rakbow.kureakurusu.util.common.DateHelper;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public void addDisc(AlbumDiscVO disc) {
        discs.add(disc);
    }

}
