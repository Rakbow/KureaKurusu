package com.rakbow.kureakurusu.data.emun.common;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.vo.RegionVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2022-12-29 21:00
 * @Description:
 */
@AllArgsConstructor
public enum Region {
    GLOBAL("Global", "全球", "global", ""),
    JAPAN("Japan", "日本", "jp", "JPY"),
    CHINA("China", "中国大陆", "cn", "CNY"),
    TAIWAN("Taiwan", "台湾地区", "tw", "TWD"),
    EUROPE("Europe", "欧洲", "eu", "EUR"),
    UNITED_STATES("United States", "美国", "us", "USD");

    @Getter
    private final String nameEn;
    @Getter
    private final String nameZh;
    @Getter
    private final String code;
    @Getter
    private final String currency;

    //根据地区代码获取货币符号
    public static String getCurrencyUnitByCode(String code){
        String currency = JAPAN.currency;
        for (Region region : Region.values()) {
            if (StringUtils.equals(region.code, code)) {
                currency = region.currency;
            }
        }
        return currency;
    }

    //根据地区代码获取地区名称
    public static String getNameByCode(String code, String lang){
        for (Region region : Region.values()) {
            if (StringUtils.equals(region.code, code)) {
                if(StringUtils.equals(lang, Locale.ENGLISH.getLanguage())) {
                    return region.nameEn;
                }else {
                    return region.nameZh;
                }
            }
        }
        return null;
    }

    /**
     * 获取地区数组
     *
     * @return list
     * @author rakbow
     */
    public static JSONArray getAttributeSet(String lang) {
        JSONArray set = new JSONArray();
        if(StringUtils.equals(lang, Locale.ENGLISH.getLanguage())) {
            for (Region region : Region.values()) {
                JSONObject jo = new JSONObject();
                jo.put("name", region.nameEn);
                jo.put("code", region.code);
                jo.put("currency", region.currency);
                set.add(jo);
            }
        }
        else if(StringUtils.equals(lang, Locale.CHINESE.getLanguage())) {
            for (Region region : Region.values()) {
                JSONObject jo = new JSONObject();
                jo.put("name", region.nameZh);
                jo.put("code", region.code);
                jo.put("currency", region.currency);
                set.add(jo);
            }
        }
        return set;
    }

    public static RegionVO getRegion(String code) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        return new RegionVO(code, Region.getNameByCode(code, lang));
    }

}
