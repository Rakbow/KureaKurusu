package com.rakbow.kureakurusu.data.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/1/7 1:42
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "entityType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SubjectUpdateDTO.class, name = "1"),
        @JsonSubTypes.Type(value = PersonUpdateDTO.class, name = "2"),
        @JsonSubTypes.Type(value = CharaUpdateDTO.class, name = "5"),
        @JsonSubTypes.Type(value = ProductUpdateDTO.class, name = "99"),
})
public class EntryUpdateDTO extends DTO {

    private Integer entityType;

    private Long id;
    @NotBlank(message = "{entity.crud.name.required_field}")
    private String name;
    private String nameZh;
    private String nameEn;
    private List<String> aliases;
    private List<String> links;
    private String remark;

}
