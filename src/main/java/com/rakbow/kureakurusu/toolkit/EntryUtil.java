package com.rakbow.kureakurusu.toolkit;

import com.rakbow.kureakurusu.data.entity.Entry;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Rakbow
 * @since 2025/6/17 18:07
 */
public class EntryUtil {

    public static String getSubName(Entry entry) {
        if(StringUtils.isNotEmpty(entry.getNameZh())) return entry.getNameZh();
        else if(StringUtils.isNotEmpty(entry.getNameEn())) return entry.getNameEn();
        else return StringUtils.EMPTY;
    }

}
