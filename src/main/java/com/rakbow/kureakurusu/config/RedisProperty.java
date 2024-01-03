package com.rakbow.kureakurusu.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Rakbow
 * @since 2023-01-08 22:13 对Redis进行配置
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperty {
    Long cacheExpire;
}
