package com.rakbow.kureakurusu.util.convertMapper.entity;

import com.rakbow.kureakurusu.util.I18nHelper;
import org.mapstruct.Named;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-11-05 4:03
 * @Description:
 */
public interface CommonVOMapper {

    @Named("getEnumLabel")
    default String getEnumLabel(String labelKey) {
        return I18nHelper.getMessage(labelKey);
    }

}
