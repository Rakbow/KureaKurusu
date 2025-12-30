package com.rakbow.kureakurusu.toolkit;

import lombok.SneakyThrows;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtil {

    private static final ObjectMapper mapper = SpringUtil.getBean(ObjectMapper.class);

    @SneakyThrows
    public static <T> List<T> toJavaList(String json, Class<T> clazz) {
        return mapper.readValue(json, mapper.getTypeFactory().constructParametricType(List.class, clazz));
    }

    // @SneakyThrows
    // public static <T> List<T> toJavaList(Object obj, Class<T> clazz) {
    //     return mapper.readValue(obj.toString(), mapper.getTypeFactory().constructParametricType(List.class, clazz));
    // }
    //
    // @SneakyThrows
    // public static <T> List<Attribute<T>> toAttributes(String json, Class<T> clazz) {
    //     JavaType attributeType = mapper.getTypeFactory().constructParametricType(Attribute.class, clazz);
    //     JavaType listType = mapper.getTypeFactory().constructCollectionType(List.class, attributeType);
    //     return mapper.readValue(json, listType);
    // }

    @SneakyThrows
    public static <T> T to(String json, Class<T> clazz) {
        return mapper.readValue(json, clazz);
    }

    @SneakyThrows
    public static <T> T to(Map<String, String> dic, Class<T> clazz) {
        return mapper.convertValue(dic, clazz);
    }

    @SneakyThrows
    public static <K, V> HashMap<K, V> toMap(String json, Class<K> kClazz, Class<V> vClazz) {
        return mapper.readValue(json, mapper.getTypeFactory().constructMapType(HashMap.class, kClazz, vClazz));
    }

    @SneakyThrows
    public static String toJson(Object obj) {
        return mapper.writeValueAsString(obj);
    }

    @SneakyThrows
    public static <T> T mapToBean(Map<String, Object> map, Class<T> clazz) {
        return mapper.readValue(mapper.writeValueAsString(map), clazz);
    }

    @SneakyThrows
    public static <T> List<T> to(List<Map<String, Object>> mapList, Class<T> clazz) {
        List<T> res = new ArrayList<>();
        mapList.forEach(map -> res.add(mapToBean(map, clazz)));
        return res;
    }

}
