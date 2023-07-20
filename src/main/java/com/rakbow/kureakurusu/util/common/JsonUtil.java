package com.rakbow.kureakurusu.util.common;

import com.alibaba.fastjson2.JSON;

public class JsonUtil {

    public static int[] toIds(String json) {
        return JSON.parseObject(json, int[].class);
    }

}
