package com.rakbow.kureakurusu.toolkit;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.SneakyThrows;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.type.MapType;
import tools.jackson.databind.type.TypeFactory;

import java.util.*;

public class JsonUtil {

    private static final ObjectMapper mapper = SpringUtil.getBean(ObjectMapper.class);

    @SneakyThrows
    public static <T> List<T> toJavaList(String json, Class<T> clazz) {
        return mapper.readValue(json, mapper.getTypeFactory().constructParametricType(List.class, clazz));
    }

    @SneakyThrows
    public static <T> List<T> toJavaList(Object o, Class<T> clazz) {
        return mapper.readValue(toJson(o), mapper.getTypeFactory().constructParametricType(List.class, clazz));
    }

    @SneakyThrows
    public static <T> List<Attribute<T>> toAttributes(Object o, Class<T> clazz) {
        TypeFactory typeFactory = mapper.getTypeFactory();
        JavaType attributeType = typeFactory.constructParametricType(Attribute.class, clazz);
        JavaType listType = typeFactory.constructCollectionType(List.class, attributeType);
        return mapper.readValue(toJson(o), listType);
    }

    @SneakyThrows
    public static <T> T to(String json, Class<T> clazz) {
        return mapper.readValue(json, clazz);
    }

    @SneakyThrows
    public static <T> T to(Object json, Class<T> clazz) {
        return mapper.readValue(mapper.writeValueAsString(json), clazz);
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

    @SneakyThrows
    public static <T> Map<T, Attribute<T>> toAttributeMap(Object o, Class<T> clazz) {
        if (o == null) return Collections.emptyMap();
        TypeFactory typeFactory = mapper.getTypeFactory();
        JavaType keyType = typeFactory.constructType(clazz);
        JavaType valType = typeFactory.constructParametricType(Attribute.class, clazz);
        MapType mapType = typeFactory.constructMapType(HashMap.class, keyType, valType);
        if (o instanceof String) {
            return mapper.readValue((String) o, mapType);
        } else if (o instanceof Map) {
            return mapper.convertValue(o, mapType);
        } else {
            return mapper.convertValue(o, mapType);
        }
    }
}
