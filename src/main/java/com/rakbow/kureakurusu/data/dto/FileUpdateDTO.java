package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.entity.resource.FileInfo;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2025/6/8 10:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoMapper(target = FileInfo.class, reverseConvertGenerate = false)
public class FileUpdateDTO extends DTO {

    private long id;
    @NotBlank(message = "{entity.crud.name.required_field}")
    private String name;
    private String remark;

}
