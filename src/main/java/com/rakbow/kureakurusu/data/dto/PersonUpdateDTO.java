package com.rakbow.kureakurusu.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.entity.entry.Person;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AutoMappers({
        @AutoMapper(target = Person.class, reverseConvertGenerate = false)
})
public class PersonUpdateDTO extends EntryUpdateDTO {

    private Integer gender;
    private String birthDate;

    public PersonUpdateDTO() {
        setEntityType(EntityType.PERSON.getValue());
        gender = 0;
        birthDate = "";
    }

}
