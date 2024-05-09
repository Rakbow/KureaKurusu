package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.entity.Product;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;

/**
 * @author Rakbow
 * @since 2024/01/17 21:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoMapper(target = Product.class, reverseConvertGenerate = false)
public class ProductCreateDTO extends DTO {

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
