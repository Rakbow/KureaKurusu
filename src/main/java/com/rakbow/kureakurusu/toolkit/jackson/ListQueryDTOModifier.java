package com.rakbow.kureakurusu.toolkit.jackson;

import com.rakbow.kureakurusu.data.dto.ListQueryDTO;
import lombok.SneakyThrows;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.BeanDescription;
import tools.jackson.databind.DeserializationConfig;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.deser.ValueDeserializerModifier;
import tools.jackson.databind.deser.std.DelegatingDeserializer;

/**
 * @author Rakbow
 * @since 2025/12/30 22:46
*/
public class ListQueryDTOModifier extends ValueDeserializerModifier {

    @Override
    public ValueDeserializer<?> modifyDeserializer(
            DeserializationConfig config,
            BeanDescription.Supplier beanDescRef,
            ValueDeserializer<?> deserializer) {

        if (ListQueryDTO.class.isAssignableFrom(beanDescRef.get().getBeanClass())) {
            return new ListQueryDTODeserializer(deserializer);
        }

        return deserializer;
    }

    /**
     * 自定义 DelegatingDeserializer
     */
    public static class ListQueryDTODeserializer extends DelegatingDeserializer {

        public ListQueryDTODeserializer(ValueDeserializer<?> delegate) {
            super(delegate);
        }

        @Override
        protected ValueDeserializer<?> newDelegatingInstance(ValueDeserializer<?> newDelegatee) {
            return new ListQueryDTODeserializer(newDelegatee);
        }

        @SneakyThrows
        @Override
        public Object deserialize(JsonParser p, DeserializationContext context) {
            Object o = super.deserialize(p, context);
            if (o instanceof ListQueryDTO dto) dto.init();
            return o;
        }
    }
}
