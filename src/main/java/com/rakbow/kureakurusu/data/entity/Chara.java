package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.Link;
import com.rakbow.kureakurusu.data.emun.Gender;
import com.rakbow.kureakurusu.data.entity.common.MetaEntity;
import com.rakbow.kureakurusu.data.vo.entry.CharacterVO;
import com.rakbow.kureakurusu.toolkit.handler.LinkHandler;
import com.rakbow.kureakurusu.toolkit.handler.StrListHandler;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.github.linpeilie.annotations.AutoMapping;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/6/22 12:44
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "chara", autoResultMap = true)
@NoArgsConstructor
@AutoMappers({
        @AutoMapper(target = CharacterVO.class, reverseConvertGenerate = false)
})
public class Chara extends MetaEntity {

    private Long id;
    private String name;
    private String nameZh;
    private String nameEn;
    @TableField(typeHandler = StrListHandler.class)
    private List<String> aliases;
    @AutoMapping(qualifiedByName = "toAttribute")
    private Gender gender;
    private String birthDate;
    @TableField(typeHandler = LinkHandler.class)
    @AutoMapping(qualifiedByName = "getLinks")
    private List<Link> links;

    // private String favorites;
    // private String hates;
    // private String bloodGroup;

}
