package com.rakbow.kureakurusu.data;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-11-05 5:17
 * @Description:
 */
public class MetaData {

    public static MetaOption optionsZh;
    public static MetaOption optionsEn;

    public static MetaOption getOptions() {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        if(lang.equals("zh"))
            return optionsZh;
        if(lang.equals("en"))
            return optionsEn;
        else return null;
    }
}
