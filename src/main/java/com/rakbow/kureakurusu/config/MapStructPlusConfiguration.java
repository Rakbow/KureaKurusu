package com.rakbow.kureakurusu.config;

import io.github.linpeilie.annotations.ComponentModelConfig;
import io.github.linpeilie.annotations.MapperConfig;
import org.springframework.context.annotation.Configuration;

/**
 * @author Rakbow
 * @since 2024/4/29 17:57
 */
@Configuration
@ComponentModelConfig(componentModel = "default")
@MapperConfig(mapperPackage = "com.rakbow.kureakurusu.util.convertMapper")
public class MapStructPlusConfiguration {
}