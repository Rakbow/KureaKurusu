package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2025/5/20 18:45
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EntryListQueryDTO extends ListQueryDTO {

    private int type;
    private String name;
    private String nameZh;
    private String nameEn;

    public EntryListQueryDTO(ListQuery qry) {
        super(qry);
        type = qry.getVal("type");
        name = qry.getVal("name");
        nameZh = qry.getVal("nameZh");
        nameEn = qry.getVal("nameEn");
    }

}
