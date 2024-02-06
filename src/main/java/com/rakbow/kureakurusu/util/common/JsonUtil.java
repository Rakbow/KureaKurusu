package com.rakbow.kureakurusu.util.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public static <T> List<T> toJavaList(String json, Class<T> clazz) {
        return mapper.readValue(json, mapper.getTypeFactory().constructParametricType(List.class, clazz));
    }

    @SneakyThrows
    public static <T> T to(String json, Class<T> clazz) {
        return mapper.readValue(json, clazz);
    }

    @SneakyThrows
    public static String toJson(Object obj) {
        return mapper.writeValueAsString(obj);
    }

    @SneakyThrows
    public static Object getValueByKey(String key, String json) {
        return mapper.readTree(json).get(key);
    }

    @SneakyThrows
    public static <T> List<T> getValueByKey(String json, Class<T> clazz, String key) {
        JsonNode rootNode = mapper.readTree(json);

        List<T> values = new ArrayList<>();

        if (rootNode.isObject()) {
            JsonNode valueNode = rootNode.get(key);
            if (valueNode != null && valueNode.isArray()) {
                ArrayNode arrayNode = (ArrayNode) valueNode;
                for (JsonNode node : arrayNode) {
                    if (clazz == String.class && node.isTextual()) {
                        values.add(clazz.cast(node.textValue()));
                    } else if (clazz == Integer.class && node.isInt()) {
                        values.add(clazz.cast(node.intValue()));
                    } else if (clazz == Double.class && node.isDouble()) {
                        values.add(clazz.cast(node.doubleValue()));
                    } else if (clazz == Boolean.class && node.isBoolean()) {
                        values.add(clazz.cast(node.booleanValue()));
                    }
                }
            }
        }

        return values;
    }

}
