package com.rakbow.kureakurusu.data.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.rakbow.kureakurusu.data.emun.Currency;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/4/25 14:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AlbumUpdateDTO.class, name = "1"),
        @JsonSubTypes.Type(value = BookUpdateDTO.class, name = "2")
})
public class ItemUpdateDTO extends DTO {

    private Long id;
    private Integer type;

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
    private boolean hasBonus;

    public ItemUpdateDTO() {
        name = "";
        nameZh = "";
        nameEn = "";
        ean13 = "";
        releaseDate = DateHelper.DEFAULT_DATE;
        price = 0;
        currency = Currency.JPY.getValue();
        remark = "";
        hasBonus = false;
    }

}
