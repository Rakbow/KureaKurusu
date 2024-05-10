package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.entity.Person;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMapping;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2024-01-04 0:03
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoMapper(target = Person.class, reverseConvertGenerate = false)
public class PersonAddDTO extends DTO {

    @NotBlank(message = "{entity.crud.name.required_field}")
    private String name;
    private String nameZh;
    private String nameEn;
    private List<String> aliases;
    private String birthDate;
    @AutoMapping(qualifiedByName = "getGender")
    private int gender;
    private String remark;

    public PersonAddDTO() {
        aliases = new ArrayList<>();
    }

}
