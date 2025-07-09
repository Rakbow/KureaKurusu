package com.rakbow.kureakurusu.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.entity.Entry;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMapping;
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
@JsonIgnoreProperties(ignoreUnknown = true)
@AutoMapper(target = Entry.class, reverseConvertGenerate = false)
public class EntryUpdateDTO extends DTO {

    private Long id;
    @NotBlank(message = "{entity.crud.name.required_field}")
    private String name;
    private String nameZh;
    private String nameEn;
    private String date;
    @AutoMapping(source = "gender.value")
    private Attribute<Integer> gender;
    private List<String> aliases;
    private List<String> links;
    private String remark;

}
