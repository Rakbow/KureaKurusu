package com.rakbow.kureakurusu.toolkit.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.rakbow.kureakurusu.data.dto.ListQueryDTO;

import java.io.IOException;

/**
 * @author Rakbow
 * @since 2025/9/1 14:59
 */
public class ListQueryDTOModifier extends BeanDeserializerModifier {

    @Override
    public JsonDeserializer<?> modifyDeserializer(
            DeserializationConfig config,
            BeanDescription beanDesc,
            JsonDeserializer<?> deserializer) {

        // 只处理 ListQueryDTO 及其子类
        if (ListQueryDTO.class.isAssignableFrom(beanDesc.getBeanClass()) && deserializer instanceof BeanDeserializer original) {

            return new BeanDeserializer(original) {
                @Override
                public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                    Object obj = super.deserialize(p, ctxt); // 调用原始反序列化逻辑
                    if (obj instanceof ListQueryDTO) {
                        ((ListQueryDTO) obj).init(); // 调用子类 init()
                    }
                    return obj;
                }
            };
        }

        return deserializer;
    }
}
