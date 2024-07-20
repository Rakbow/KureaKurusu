package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.OrderBy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.Link;
import com.rakbow.kureakurusu.data.dto.PersonUpdateDTO;
import com.rakbow.kureakurusu.data.emun.Gender;
import com.rakbow.kureakurusu.data.entity.common.MetaEntity;
import com.rakbow.kureakurusu.data.vo.person.PersonVO;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.handler.LinkHandler;
import com.rakbow.kureakurusu.toolkit.handler.StrListHandler;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.github.linpeilie.annotations.AutoMapping;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2023-10-06 3:59
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@TableName(value = "person", autoResultMap = true)
@AutoMappers({
        @AutoMapper(target = PersonVO.class, reverseConvertGenerate = false)
})
public class Person extends MetaEntity {

    @OrderBy
    private Long id;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long mfcEntryId;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long bgmPersonId;
    @NotBlank(message = "{entity.crud.name.required_field}")
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String name;
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String nameEn;
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String nameZh;
    @TableField(typeHandler = StrListHandler.class)
    private List<String> aliases;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String cover;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String image;
    @AutoMapping(qualifiedByName = "toAttribute")
    private Gender gender;
    private String birthDate;
    @TableField(typeHandler = LinkHandler.class)
    @AutoMapping(qualifiedByName = "getLinks")
    private List<Link> links;
    private String info;

    public Person() {
        id = 0L;
        name = "";
        nameEn = "";
        nameZh = "";
        aliases = new ArrayList<>();
        cover = "";
        image = "";
        gender = Gender.UNKNOWN;
        birthDate = "";
        links = new ArrayList<>();
        info = "{}";
        setDetail("");
        setRemark("");
        setAddedTime(DateHelper.now());
        setEditedTime(DateHelper.now());
        setStatus(true);
    }

    public Person(PersonUpdateDTO dto) {
        id = dto.getId();
        name = dto.getName();
        nameZh = dto.getNameZh();
        nameEn = dto.getNameEn();
        aliases = dto.getAliases();
        gender = Gender.get(dto.getGender());
        birthDate = dto.getBirthDate();
        setRemark(dto.getRemark());
        updateEditedTime();
    }

}
