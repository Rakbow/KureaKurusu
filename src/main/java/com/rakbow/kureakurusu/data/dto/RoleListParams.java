package com.rakbow.kureakurusu.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Rakbow
 * @since 2024/3/18 0:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleListParams extends ListQueryParams {

    private String name;
    private String nameZh;
    private String nameEn;

    public RoleListParams(ListQueryDTO qry) {
        super(qry);
        this.name = super.getVal("name");
        this.nameZh = super.getVal("nameZh");
        this.nameEn = super.getVal("nameEn");
    }

}
