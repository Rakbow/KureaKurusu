package com.rakbow.kureakurusu.data.dto.franchise;

import com.rakbow.kureakurusu.data.dto.base.DTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @author Rakbow
 * @since 2024/01/26 0:38
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FranchiseUpdateDTO extends DTO {

    private long id;
    @NotBlank(message = "{entity.crud.name.required_field}")
    private String name;
    @NotBlank(message = "{entity.crud.name_zh.required_field}")
    private String nameZh;
    @NotBlank(message = "{entity.crud.name_en.required_field}")
    private String nameEn;
    private String remark;

}
