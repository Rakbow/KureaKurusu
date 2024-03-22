package com.rakbow.kureakurusu.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Rakbow
 * @since 2024/3/22 14:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FranchiseListParams extends QueryParams {

    private String name;
    private String nameZh;
    private String nameEn;

    public FranchiseListParams(ListQry qry) {
        super(qry);
        this.name = super.getStr("name");
        this.nameZh = super.getStr("nameZh");
        this.nameEn = super.getStr("nameEn");
    }

}