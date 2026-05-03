package com.rakbow.kureakurusu.data.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.github.linpeilie.annotations.AutoEnumMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2026/5/3 23:16
 */
@Getter
@AllArgsConstructor
@AutoEnumMapper("value")
public enum EntityResourceType {

    LOCAL(1),
    CLOUD(2);

    @EnumValue
    private final Integer value;

}
