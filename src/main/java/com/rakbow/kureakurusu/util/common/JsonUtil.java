package com.rakbow.kureakurusu.util.common;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.util.List;

public class JsonUtil {

    public static int[] toIds(String json) {
        return JSON.parseObject(json, int[].class);
    }

    public static <T> List<T> toJavaList(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }

    public static <T> T to(String json, Class<T> clazz) {
        return JSON.to(clazz, json);
    }

    public static String toJson(Object obj) {
        return JSON.toJSONString(obj);
    }

    public static Object getValueByKey(String key, String json) {
        return JSONObject.parseObject(json).get(key);
    }

    public static <T> List<T> getValueByKey(String json, Class<T> clazz, String key) {
        return JSONObject.parseObject(json).getList(key, clazz);
    }

}
