package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.rakbow.kureakurusu.data.emun.FigureType;
import com.rakbow.kureakurusu.data.entity.common.SuperItem;
import com.rakbow.kureakurusu.data.vo.item.FigureListVO;
import com.rakbow.kureakurusu.data.vo.item.FigureVO;
import com.rakbow.kureakurusu.toolkit.handler.StrListHandler;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.github.linpeilie.annotations.AutoMapping;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2024/7/26 17:29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AutoMappers({
        @AutoMapper(target = FigureListVO.class, reverseConvertGenerate = false),
        @AutoMapper(target = FigureVO.class, reverseConvertGenerate = false)
})
public class Figure extends SuperItem {

    @AutoMapping(qualifiedByName = "toAttribute")
    @TableField(whereStrategy = FieldStrategy.NOT_EMPTY)
    private FigureType figureType;
    private String scale;
    private String various;
    private String title;
    private String titleEn;
    @TableField(typeHandler = StrListHandler.class)
    private List<String> versions;
    @TableField(typeHandler = StrListHandler.class)
    private List<String> versionsEn;

    public Figure() {
        super();
        figureType = FigureType.PREPAINTED;
        scale = "";
        various = "";
        title = "";
        titleEn = "";
        versions = new ArrayList<>();
        versionsEn = new ArrayList<>();
    }

}
