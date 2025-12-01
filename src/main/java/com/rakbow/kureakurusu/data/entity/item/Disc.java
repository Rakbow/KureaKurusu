package com.rakbow.kureakurusu.data.entity.item;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.enums.MediaFormat;
import com.rakbow.kureakurusu.data.entity.Entity;
import com.rakbow.kureakurusu.data.vo.item.DiscVO;
import com.rakbow.kureakurusu.toolkit.handler.IntegerListHandler;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMapping;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/6/24 12:39
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "disc", autoResultMap = true)
@NoArgsConstructor
@AutoMapper(target = DiscVO.class, reverseConvertGenerate = false)
public class Disc extends Entity {

    private long itemId;// album id
    private String catalogId;
    private int discNo;
    @AutoMapping(qualifiedByName = "toAttribute")
    private MediaFormat mediaFormat;
    @TableField(typeHandler = IntegerListHandler.class)
    @AutoMapping(qualifiedByName = "getAlbumFormat")
    private List<Integer> albumFormat;

}
