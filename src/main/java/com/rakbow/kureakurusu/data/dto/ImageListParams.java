package com.rakbow.kureakurusu.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Rakbow
 * @since 2024/5/28 17:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageListParams extends ListQueryParams {

    private int type;
    private int entityType;
    private long entityId;

    public ImageListParams(ListQueryDTO qry) {
        super(qry);
        this.type = super.getInteger("type");
        this.entityType = super.getInteger("entityType");
        this.entityId = super.getInteger("entityId");
    }

}