package com.rakbow.kureakurusu.util.jackson;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;

public class BooleanToIntDeserializer extends JsonDeserializer<Integer> {
    @Override
    public Integer deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException {
        boolean boolValue = p.getBooleanValue();
        return boolValue ? 1 : 0;
    }
}