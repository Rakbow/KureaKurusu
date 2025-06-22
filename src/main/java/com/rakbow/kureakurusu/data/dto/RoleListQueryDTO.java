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
public class RoleListQueryDTO extends ListQueryDTO {

    private String keyword;

    public RoleListQueryDTO(ListQuery qry) {
        super(qry);
        this.keyword = qry.getVal("keyword");
    }

}
