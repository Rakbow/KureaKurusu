package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.rakbow.kureakurusu.data.emun.ChangelogField;
import com.rakbow.kureakurusu.data.emun.ChangelogOperate;
import com.rakbow.kureakurusu.data.vo.ChangelogVO;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.convert.GlobalConverters;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author Rakbow
 * @since 2025/7/22 21:28
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName(value = "changelog", autoResultMap = true)
@AutoMappers({
        @AutoMapper(target = ChangelogVO.class, reverseConvertGenerate = false, uses = GlobalConverters.class)
})
public class Changelog {

    private Long id;
    private int entityType;
    private long entityId;
    private ChangelogField field;
    private String oldValue;
    private String newValue;
    private ChangelogOperate operate;
    private String operator;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateHelper.DATE_TIME_FORMAT, timezone="GMT+8")
    @TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER)
    private Timestamp operateTime;

}
