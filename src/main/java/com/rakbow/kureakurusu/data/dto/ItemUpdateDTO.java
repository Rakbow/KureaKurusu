package com.rakbow.kureakurusu.data.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

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
        @JsonSubTypes.Type(value = BookUpdateDTO.class, name = "2"),
        @JsonSubTypes.Type(value = DiscUpdateDTO.class, name = "3")
})
public class ItemUpdateDTO extends DTO {

    private Long id;
    private Integer type;
    private Integer subType;

    @NotBlank(message = "{entity.crud.name.required_field}")
    private String name;
    private List<String> aliases;

    private int releaseType;
    private String barcode;
    private String catalogId;
    private String releaseDate;

    private double width;// mm
    private double length;// mm
    private double height;// mm
    private double weight;// g

    private double price;
    private String region;

    private String remark;
    private boolean bonus;

}
