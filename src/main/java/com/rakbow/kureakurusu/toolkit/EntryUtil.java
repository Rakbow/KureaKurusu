package com.rakbow.kureakurusu.toolkit;

import com.rakbow.kureakurusu.data.entity.Entry;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Rakbow
 * @since 2025/6/17 18:07
 */
public class EntryUtil {

    public static String getSubName(String nameZh, String nameEn) {
        if(StringUtils.isNotEmpty(nameZh)) return nameZh;
        else if(StringUtils.isNotEmpty(nameEn)) return nameEn;
        else return StringUtils.EMPTY;
    }

}
