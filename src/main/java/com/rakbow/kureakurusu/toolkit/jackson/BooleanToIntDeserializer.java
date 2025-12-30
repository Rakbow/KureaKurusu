package com.rakbow.kureakurusu.toolkit.jackson;

import tools.jackson.databind.DeserializationContext;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.ValueDeserializer;

public class BooleanToIntDeserializer extends ValueDeserializer<Integer> {
    @Override
    public Integer deserialize(JsonParser p, DeserializationContext deserializationContext) {
        boolean boolValue = p.getBooleanValue();
        return boolValue ? 1 : 0;
    }
}