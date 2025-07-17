package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.entity.Entry;
import com.rakbow.kureakurusu.toolkit.convert.GlobalConverters;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/7/17 16:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoMapper(target = Entry.class, reverseConvertGenerate = false, uses = GlobalConverters.class)
public class EntryCreateDTO extends DTO {

    private long id;
    private Integer type;
    private Integer subType;

    @NotBlank(message = "{entity.crud.name.required_field}")
    private String name;
    private String nameZh;
    private String nameEn;
    private List<String> aliases;
    private List<String> links;

    private int gender;
    private String date;

    private String detail;
    private String remark;

}
