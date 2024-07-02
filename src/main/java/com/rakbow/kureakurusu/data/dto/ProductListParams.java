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
    private List<Integer> type;

    public ProductListParams(ListQueryDTO qry) {
        super(qry);
        this.name = super.getVal("name");
        this.type = super.getVal("type");
    }

}