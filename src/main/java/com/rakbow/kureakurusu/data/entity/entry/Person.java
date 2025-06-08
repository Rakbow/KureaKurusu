package com.rakbow.kureakurusu.data.entity.entry;

import com.baomidou.mybatisplus.annotation.*;
import com.rakbow.kureakurusu.data.emun.Gender;
import com.rakbow.kureakurusu.data.vo.entry.PersonListVO;
import com.rakbow.kureakurusu.data.vo.entry.PersonVO;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.github.linpeilie.annotations.AutoMapping;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Rakbow
 * @since 2023-10-06 3:59
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "person", autoResultMap = true)
@NoArgsConstructor
@AutoMappers({
        @AutoMapper(target = PersonListVO.class, reverseConvertGenerate = false),
        @AutoMapper(target = PersonVO.class, reverseConvertGenerate = false)
})
public class Person extends Entry {

    @TableId(type = IdType.AUTO)
    private Long id = 0L;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long mfcEntryId;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long bgmPersonId;

    @AutoMapping(qualifiedByName = "toAttribute")
    private Gender gender;
    private String birthDate;

}
