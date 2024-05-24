package com.rakbow.kureakurusu.data.emun;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.github.linpeilie.annotations.AutoEnumMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2022-11-18 23:01
 */
@Getter
@AllArgsConstructor
@AutoEnumMapper("value")
public enum ImageType {

    DEFAULT(0),
    THUMB(1),
    MAIN(2),
    OTHER(99);

    @EnumValue
    private final Integer value;

}
