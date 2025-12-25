package com.rakbow.kureakurusu.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rakbow.kureakurusu.data.entity.Entry;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/1/7 1:42
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@AutoMapper(target = Entry.class, reverseConvertGenerate = false)
public record EntryUpdateDTO(
        Long id,
        @NotBlank(message = "{entity.crud.name.required_field}")
        String name,
        String nameZh,
        String nameEn,
        String date,
        Integer gender,
        List<String> aliases,
        List<String> links,
        String remark
) {
}
