package com.rakbow.kureakurusu.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/3/17 7:11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonListParams extends ListQueryParams {

    private String name;
    private String nameZh;
    private String nameEn;
    private String aliases;
    private List<Integer> gender;

    public PersonListParams(ListQueryDTO qry) {
        super(qry);
        this.name = super.getVal("name");
        this.nameZh = super.getVal("nameZh");
        this.nameEn = super.getVal("nameEn");
        this.aliases = super.getVal("aliases");
        this.gender = super.getVal("gender");
    }

}
