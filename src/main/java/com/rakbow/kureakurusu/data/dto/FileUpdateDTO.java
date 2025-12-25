package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.entity.resource.FileInfo;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;

/**
 * @author Rakbow
 * @since 2025/6/8 10:22
 */
@AutoMapper(target = FileInfo.class, reverseConvertGenerate = false)
public record FileUpdateDTO(
        long id,
        @NotBlank(message = "{entity.crud.name.required_field}")
        String name,
        String remark
) {
}
