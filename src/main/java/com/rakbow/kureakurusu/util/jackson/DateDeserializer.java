package com.rakbow.kureakurusu.util.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateDeserializer extends JsonDeserializer<Date> {

    private static final String[] dateFormats = {"yyyy/MM/dd", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"};

    private static Date parseDate(String dateString) throws ParseException {
        for (String dateFormat : dateFormats) {
            try {
                return new SimpleDateFormat(dateFormat).parse(dateString);
            } catch (ParseException e) {
            }
        }
        throw new ParseException("unknown date format : \"" + dateString + "\"", 0);
    }

    @Override
    public Date deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException {
        String dateString = p.getValueAsString().trim();
        try {
            return parseDate(dateString);
        } catch (ParseException e) {
            throw new IOException(e);
        }
    }
}
