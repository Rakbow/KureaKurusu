package com.rakbow.kureakurusu.data.entity.item;

import com.baomidou.mybatisplus.annotation.TableField;
import com.rakbow.kureakurusu.data.vo.item.DiscListVO;
import com.rakbow.kureakurusu.data.vo.item.DiscVO;
import com.rakbow.kureakurusu.toolkit.handler.IntegerListHandler;
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
 * @since 2022-11-27 18:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@AutoMappers({
        @AutoMapper(target = DiscListVO.class, reverseConvertGenerate = false),
        @AutoMapper(target = DiscVO.class, reverseConvertGenerate = false)
})
public class Disc extends SuperItem {

    @AutoMapping(qualifiedByName = "getMediaFormat")
    @TableField(typeHandler = IntegerListHandler.class)
    private List<Integer> mediaFormat;//媒体类型

    private int discs;
    private int episodes;
    private int runTime;

    public Disc() {
        super();
        this.mediaFormat = new ArrayList<>();
    }
}
