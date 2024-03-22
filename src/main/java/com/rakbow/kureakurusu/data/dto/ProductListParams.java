package com.rakbow.kureakurusu.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/3/22 13:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductListParams extends QueryParams {

    private String name;
    private String nameZh;
    private String nameEn;
    private List<Integer> category;

    public ProductListParams(ListQry qry) {
        super(qry);
        this.name = super.getStr("name");
        this.nameZh = super.getStr("nameZh");
        this.nameEn = super.getStr("nameEn");
        this.category = super.getArray("category");
    }

}