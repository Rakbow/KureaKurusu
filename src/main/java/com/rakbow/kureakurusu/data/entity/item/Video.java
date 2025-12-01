package com.rakbow.kureakurusu.data.entity.item;

import com.baomidou.mybatisplus.annotation.TableField;
import com.rakbow.kureakurusu.data.vo.item.VideoListVO;
import com.rakbow.kureakurusu.data.vo.item.VideoVO;
import com.rakbow.kureakurusu.toolkit.convert.GlobalConverters;
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
        @AutoMapper(target = VideoListVO.class, reverseConvertGenerate = false, uses = GlobalConverters.class),
        @AutoMapper(target = VideoVO.class, reverseConvertGenerate = false, uses = GlobalConverters.class)
})
public class Video extends SuperItem {

    private int discs;
    private int episodes;
    private int runTime;

    public Video() {
        super();
    }
}
