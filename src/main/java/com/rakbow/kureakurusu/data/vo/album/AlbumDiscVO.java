package com.rakbow.kureakurusu.data.vo.album;

import com.rakbow.kureakurusu.data.emun.DataActionType;
import com.rakbow.kureakurusu.util.common.DateHelper;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.rakbow.kureakurusu.data.common.Constant.BAR;
import static com.rakbow.kureakurusu.data.common.Constant.EMPTY;

/**
 * @author Rakbow
 * @since 2024/01/08 17:34
 */
@Data
public class AlbumDiscVO {

    private int serial;//碟片序号
    private int action;//操作
    private String code;
    private List<AlbumTrackVO> tracks;//音轨列表
    private String duration;//碟片时长 hh:mm:ss

    public AlbumDiscVO() {
        serial = 1;
        action = DataActionType.NO_ACTION.getValue();
        tracks = new ArrayList<>();
        duration = DateHelper.EMPTY_DURATION;
    }

    public void addTrack(AlbumTrackVO track) {
        tracks.add(track);
    }

    public boolean isAdd() {
        return action == DataActionType.INSERT.getValue();
    }

    public void generateCode(String catalogNo, int discNum) {
        if(StringUtils.isBlank(catalogNo))
            code = EMPTY;
        else
            code = catalogNo + BAR + discNum;
    }

}
