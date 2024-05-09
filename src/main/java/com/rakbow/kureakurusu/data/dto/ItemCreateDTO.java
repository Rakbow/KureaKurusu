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
 * @since 2024/5/3 4:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AlbumCreateDTO.class, name = "1"),
        @JsonSubTypes.Type(value = BookCreateDTO.class, name = "2")
})
public class ItemCreateDTO extends DTO {

    private long id;
    private int type;

    @NotBlank(message = "{entity.crud.name.required_field}")
    private String name;
    private String nameZh;
    private String nameEn;

    private String ean13;
    private String releaseDate;

    private double price;
    private String currency;

    private String remark;

    public ItemCreateDTO() {
        name = "";
        nameZh = "";
        nameEn = "";
        ean13 = "";
        releaseDate = DateHelper.DEFAULT_DATE;
        price = 0;
        currency = Currency.JPY.getValue();
        remark = "";
    }

}
