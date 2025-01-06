package com.rakbow.kureakurusu.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.entity.entry.Subject;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2025/1/7 1:45
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AutoMappers({
        @AutoMapper(target = Subject.class, reverseConvertGenerate = false)
})
public class SubjectUpdateDTO extends EntryUpdateDTO {

    private Integer type;
    private String date;// event-date

    public SubjectUpdateDTO() {
        setEntityType(EntityType.SUBJECT.getValue());
        type = 0;
        date = "";
    }

}
