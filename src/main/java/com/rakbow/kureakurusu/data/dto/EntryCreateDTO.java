package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.entity.Entry;
import com.rakbow.kureakurusu.toolkit.convert.GlobalConverters;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/7/17 16:30
 */
@AutoMapper(target = Entry.class, reverseConvertGenerate = false, uses = GlobalConverters.class)
public record EntryCreateDTO(
        long id,
        Integer type,
        Integer subType,
        @NotBlank(message = "{entity.crud.name.required_field}")
        String name,
        String nameZh,
        String nameEn,
        List<String> aliases,
        List<String> links,
        int gender,
        String date,
        String detail,
        String remark
) {
}
