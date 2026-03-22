package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.rakbow.kureakurusu.data.vo.index.IndexVO;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.convert.GlobalConverters;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.github.linpeilie.annotations.AutoMapping;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author Rakbow
 * @since 2025/7/24 19:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "r4_index", autoResultMap = true)
@NoArgsConstructor
@AutoMappers({
        @AutoMapper(target = IndexVO.class, reverseConvertGenerate = false, uses = GlobalConverters.class)
})
public class Index extends Entity {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(updateStrategy = FieldStrategy.NEVER)
    @AutoMapping(qualifiedByName = "entityType")
    private int type;

    @NotBlank(message = "{entity.crud.name.required_field}")
    private String name;

    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateHelper.DATE_TIME_FORMAT, timezone="GMT+8")
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    @OrderBy
    private Timestamp createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateHelper.DATE_TIME_FORMAT, timezone="GMT+8")
    private Timestamp updatedAt;

}
