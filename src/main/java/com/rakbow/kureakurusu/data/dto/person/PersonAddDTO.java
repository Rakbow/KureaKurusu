package com.rakbow.kureakurusu.data.dto.person;

import com.rakbow.kureakurusu.data.dto.base.DTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2024-01-04 0:03
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PersonAddDTO extends DTO {

    @NotBlank(message = "{entity.crud.name.required_field}")
    private String name;
    private String nameZh;
    private String nameEn;
    private List<String> aliases;
    private String birthDate;
    private int gender;
    private String remark;

    public PersonAddDTO() {
        aliases = new ArrayList<>();
    }

}
