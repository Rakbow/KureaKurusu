package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.entity.resource.Image;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/1/5 17:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoMapper(target = Image.class, reverseConvertGenerate = false)
public class ImageUpdateDTO extends DTO {

    private long id;
    private int type;
    @NotBlank(message = "{entity.crud.name.required_field}")
    private String name;
    private String nameZh;
    private String detail;

}
