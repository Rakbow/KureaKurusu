package com.rakbow.kureakurusu.data.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.rakbow.kureakurusu.data.emun.ReleaseType;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

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
    private List<String> aliases;

    private String barcode;
    private String releaseDate;
    private int releaseType;
    private double price;
    private String region;

    private boolean bonus;
    private String remark;

    public ItemCreateDTO() {
        name = "";
        aliases = new ArrayList<>();
        barcode = "";
        releaseDate = DateHelper.DEFAULT_DATE;
        releaseType = ReleaseType.STANDARD.getValue();
        region = "";
        price = 0;
        bonus = false;
        remark = "";
    }

}
