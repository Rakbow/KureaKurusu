package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.emun.ItemType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/5/3 4:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ItemCreateDTO extends DTO {

    private Long id;
    private Integer type;

    @NotBlank(message = "{entity.crud.name.required_field}")
    private String name;
    private String nameZh;
    private String nameEn;

    private String ean13;
    private String releaseDate;

    private double price;
    private String currency;

    private String remark;

}
