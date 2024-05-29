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

    private Integer type;
    private Integer entityType;
    private Integer entityId;

    public ImageListParams(ListQueryDTO qry) {
        super(qry);
        this.type = super.getVal("type");
        this.entityType = super.getVal("entityType");
        this.entityId = super.getVal("entityId");
    }

}