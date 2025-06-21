package com.rakbow.kureakurusu.data.vo.item;

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

    private int discNo;
    private int serial;
    private long id;
    private String name;
    private String nameEn;
    private String duration;

    private int action;

    public boolean isUpdate() {
        return action == DataActionType.UPDATE.getValue();
    }

    public boolean isDelete() {
        return action == DataActionType.REAL_DELETE.getValue();
    }

}
