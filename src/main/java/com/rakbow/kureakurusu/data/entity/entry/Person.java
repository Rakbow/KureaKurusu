package com.rakbow.kureakurusu.data.entity.entry;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.emun.Gender;
import com.rakbow.kureakurusu.data.vo.entry.PersonListVO;
import com.rakbow.kureakurusu.data.vo.entry.PersonVO;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.github.linpeilie.annotations.AutoMapping;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;

/**
 * @author Rakbow
 * @since 2023-10-06 3:59
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@TableName(value = "person", autoResultMap = true)
@AutoMappers({
        @AutoMapper(target = PersonListVO.class, reverseConvertGenerate = false),
        @AutoMapper(target = PersonVO.class, reverseConvertGenerate = false)
})
public class Person extends Entry {

    // @OrderBy
    private Long id;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long mfcEntryId;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long bgmPersonId;

    @AutoMapping(qualifiedByName = "toAttribute")
    private Gender gender;
    private String birthDate;

    public Person() {
        id = 0L;
        setName("");
        setNameZh("");
        setNameEn("");
        setAliases(new ArrayList<>());
        gender = Gender.UNKNOWN;
        birthDate = "";
        setLinks(new ArrayList<>());
        setDetail("");
        setRemark("");
        setEditedTime(DateHelper.now());
        setStatus(true);
    }

}
