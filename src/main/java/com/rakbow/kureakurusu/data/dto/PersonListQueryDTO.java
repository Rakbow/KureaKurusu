package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.annotation.QueryColumn;
import com.rakbow.kureakurusu.data.emun.EntrySearchType;
import com.rakbow.kureakurusu.data.entity.QueryColumnType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2025/5/21 10:14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PersonListQueryDTO extends EntryListQueryDTO {

    @QueryColumn(type = QueryColumnType.NUMBER)
    private Integer gender;
    @QueryColumn(type = QueryColumnType.STRING)
    private String birthDate;

    public PersonListQueryDTO(ListQuery dto) {
        super(dto);
        super.setSearchType(EntrySearchType.PERSON.getValue());
        this.gender = dto.getVal("gender");
        this.birthDate = dto.getVal("birthDate");
    }

}
