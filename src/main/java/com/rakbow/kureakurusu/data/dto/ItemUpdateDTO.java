package com.rakbow.kureakurusu.data.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/4/25 14:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ItemUpdateDTO extends DTO {

    private long id;
    private int type;

    @NotBlank(message = "{entity.crud.name.required_field}")
    private String name;
    private String nameZh;
    private String nameEn;

    private String ean13;

    @NotBlank(message = "{entity.crud.release_date.required_field}")
    private String releaseDate;

    private double price;
    private String currency;

    private String remark;

}
