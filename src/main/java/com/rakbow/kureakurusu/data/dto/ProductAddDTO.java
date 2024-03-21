package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;

/**
 * @author Rakbow
 * @since 2024/01/17 21:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProductAddDTO extends DTO {

    private long id;
    @NotBlank(message = "{entity.crud.name.required_field}")
    private String name;
    private String nameZh;
    private String nameEn;
    @NotBlank(message = "{entity.crud.date.required_field}")
    private String releaseDate;
    private int franchise;
    private int category;
    private String remark;

}
