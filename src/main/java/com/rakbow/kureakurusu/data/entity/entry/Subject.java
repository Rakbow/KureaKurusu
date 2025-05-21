package com.rakbow.kureakurusu.data.entity.entry;

import com.baomidou.mybatisplus.annotation.*;
import com.rakbow.kureakurusu.data.emun.SubjectType;
import com.rakbow.kureakurusu.data.vo.entry.SubjectListVO;
import com.rakbow.kureakurusu.data.vo.entry.SubjectVO;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.github.linpeilie.annotations.AutoMapping;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author Rakbow
 * @since 2024/6/18 18:12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@TableName(value = "subject", autoResultMap = true)
@AutoMappers({
        @AutoMapper(target = SubjectListVO.class, reverseConvertGenerate = false),
        @AutoMapper(target = SubjectVO.class, reverseConvertGenerate = false)
})
public class Subject extends Entry {

    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long mfcEntryId;// myFigureCollection entry id

    @AutoMapping(qualifiedByName = "toAttribute")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private SubjectType type;
    private String date;// event-date

}
