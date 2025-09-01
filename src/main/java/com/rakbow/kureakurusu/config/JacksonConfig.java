package com.rakbow.kureakurusu.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.rakbow.kureakurusu.toolkit.jackson.ListQueryDTOModifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
