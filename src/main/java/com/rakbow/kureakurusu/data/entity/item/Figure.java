package com.rakbow.kureakurusu.data.entity.item;

import com.baomidou.mybatisplus.annotation.TableField;
import com.rakbow.kureakurusu.data.vo.item.FigureListVO;
import com.rakbow.kureakurusu.data.vo.item.FigureVO;
import com.rakbow.kureakurusu.toolkit.handler.StrListHandler;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
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
        scale = "";
        various = "";
        title = "";
        titleEn = "";
        versions = new ArrayList<>();
        versionsEn = new ArrayList<>();
    }

}
