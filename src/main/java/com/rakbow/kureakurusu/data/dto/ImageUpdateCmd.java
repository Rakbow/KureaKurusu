package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.emun.DataActionType;
import com.rakbow.kureakurusu.data.image.Image;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/1/5 17:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ImageUpdateCmd extends CommonCommand {

    private int entityType;
    private long entityId;
    private int action;
    private List<Image> images;

    public boolean update() {
        return this.action == DataActionType.UPDATE.getValue();
    }

    public boolean delete() {
        return this.action == DataActionType.REAL_DELETE.getValue();
    }

}
