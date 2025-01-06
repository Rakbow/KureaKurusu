package com.rakbow.kureakurusu.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.entity.entry.Chara;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2025/1/7 1:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AutoMappers({
        @AutoMapper(target = Chara.class, reverseConvertGenerate = false)
})
public class CharaUpdateDTO extends EntryUpdateDTO {

    private Integer gender;
    private String birthDate;

    public CharaUpdateDTO() {
        setEntityType(EntityType.CHARACTER.getValue());
        gender = 0;
        birthDate = "";
    }

}
