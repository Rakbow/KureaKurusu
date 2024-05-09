package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.entity.Franchise;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/01/26 0:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoMapper(target = Franchise.class, reverseConvertGenerate = false)
public class FranchiseCreateDTO extends DTO {

    private long id;
    @NotBlank(message = "{entity.crud.name.required_field}")
    private String name;
    @NotBlank(message = "{entity.crud.name_zh.required_field}")
    private String nameZh;
    @NotBlank(message = "{entity.crud.name_en.required_field}")
    private String nameEn;
    private String remark;

}
