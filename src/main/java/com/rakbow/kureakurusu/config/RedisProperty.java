package com.rakbow.kureakurusu.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-01-08 22:13
 * @Description: 对Redis进行配置
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperty {
    Long cacheExpire;
}
