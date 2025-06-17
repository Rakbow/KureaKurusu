package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.emun.EntrySubType;
import com.rakbow.kureakurusu.data.emun.EntryType;
import com.rakbow.kureakurusu.data.emun.Gender;
import com.rakbow.kureakurusu.data.vo.entry.EntryListVO;
import com.rakbow.kureakurusu.data.vo.entry.EntryVO;
import com.rakbow.kureakurusu.toolkit.handler.StrListHandler;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.github.linpeilie.annotations.AutoMapping;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Rakbow
 * @since 2023-05-02 3:55
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@TableName(value = "entry", autoResultMap = true)
@AutoMappers({
        @AutoMapper(target = EntryListVO.class, reverseConvertGenerate = false),
        @AutoMapper(target = EntryVO.class, reverseConvertGenerate = false)
})
public class Entry extends Entity {

    @AutoMapping(qualifiedByName = "toAttribute")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private EntryType type;
    @AutoMapping(qualifiedByName = "toAttribute")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private EntrySubType subType;
    @NotBlank(message = "{entity.crud.name.required_field}")
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String name;
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String nameZh;
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private String nameEn;
    @TableField(typeHandler = StrListHandler.class)
    private List<String> aliases;
    @TableField(typeHandler = StrListHandler.class)
    private List<String> links;

    @AutoMapping(qualifiedByName = "toAttribute")
    private Gender gender;
    private String date;

    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String cover;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String thumb;

    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String detail;

    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long mfc_entry_id;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Integer bgm_type;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long bgm_id;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Integer orgEntityType;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long orgEntityId;

}
