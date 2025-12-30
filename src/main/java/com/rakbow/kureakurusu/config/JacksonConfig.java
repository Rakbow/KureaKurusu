package com.rakbow.kureakurusu.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.rakbow.kureakurusu.toolkit.jackson.ListQueryDTOModifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

/**
 * @author Rakbow
 * @since 2025/9/1 15:01
 */
@Configuration
public class JacksonConfig {

    @Bean
    public SimpleModule listQueryDTOModule() {
        return new SimpleModule().setDeserializerModifier(new ListQueryDTOModifier());
    }

    @Bean
    @Primary
    public ObjectMapper jacksonJsonMapper() {
        return JsonMapper.builder()
                .changeDefaultVisibility(vc -> vc
                        .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                        .withGetterVisibility(JsonAutoDetect.Visibility.ANY)
                        .withSetterVisibility(JsonAutoDetect.Visibility.ANY)
                        .withCreatorVisibility(JsonAutoDetect.Visibility.ANY)
                        .withIsGetterVisibility(JsonAutoDetect.Visibility.ANY)
                )
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false)
                .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
                .build();
    }
}
