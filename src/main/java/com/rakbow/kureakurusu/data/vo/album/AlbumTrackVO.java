package com.rakbow.kureakurusu.data.vo.album;

import com.rakbow.kureakurusu.data.emun.DataActionType;
import lombok.Builder;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2024/01/08 17:35
 */
@Builder
@Data
public class AlbumTrackVO {

    private int serial;//音轨序号
    private long id;//Episode Id
    private String title;//音轨标题(原)
    private String titleEn;//音轨标题(英)
    private String duration;//音轨时长 hh:mm:ss

    private int action;

    public boolean isUpdate() {
        return action == DataActionType.UPDATE.getValue();
    }

    public boolean isDelete() {
        return action == DataActionType.REAL_DELETE.getValue();
    }

}
