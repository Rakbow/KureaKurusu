package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.entity.entry.Product;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/01/17 21:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoMapper(target = Product.class, reverseConvertGenerate = false)
public class ProductCreateDTO extends DTO {

    private long id;
    private int type;
    @NotBlank(message = "{entity.crud.name.required_field}")
    private String name;
    private String nameZh;
    private String nameEn;
    private List<String> aliases;
    private String date;
    private String remark;

}
