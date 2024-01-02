package com.rakbow.kureakurusu.data;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-10-30 17:51
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum Gender implements IEnum<Integer> {

    UNKNOWN(0, "enum.gender.unknown"),
    MALE(1, "enum.gender.male"),
    FEMALE(2, "enum.gender.female");

    @EnumValue
    private final Integer value;
    private final String labelKey;

}
