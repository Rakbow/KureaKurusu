package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.entity.resource.Image;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;

/**
 * @author Rakbow
 * @since 2024/1/5 17:33
 */
@AutoMapper(target = Image.class, reverseConvertGenerate = false)
public record ImageUpdateDTO(
        long id,
        int type,
        @NotBlank(message = "{entity.crud.name.required_field}")
        String name,
        String nameZh,
        String detail
) {
}
