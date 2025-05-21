package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.annotation.QueryColumn;
import com.rakbow.kureakurusu.data.emun.EntrySearchType;
import com.rakbow.kureakurusu.data.entity.QueryColumnType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2025/5/21 10:11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SubjectListQueryDTO extends EntryListQueryDTO {

    @QueryColumn(type = QueryColumnType.NUMBER)
    private Integer type;
    @QueryColumn(type = QueryColumnType.STRING)
    private String date;

    public SubjectListQueryDTO(ListQueryDTO dto) {
        super(dto);
        super.setSearchType(EntrySearchType.CLASSIFICATION.getValue());
        this.type = dto.getVal("type");
        this.date = dto.getVal("date");
    }

}
