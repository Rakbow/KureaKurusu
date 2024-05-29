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
public class ProductListParams extends ListQueryParams {

    private String name;
    private String nameZh;
    private String nameEn;
    private List<Integer> category;

    public ProductListParams(ListQueryDTO qry) {
        super(qry);
        this.name = super.getVal("name");
        this.nameZh = super.getVal("nameZh");
        this.nameEn = super.getVal("nameEn");
        this.category = super.getVal("category");
    }

}