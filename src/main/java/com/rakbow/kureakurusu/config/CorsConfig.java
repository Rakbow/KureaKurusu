package com.rakbow.kureakurusu.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Rakbow
 * @since 2023-07-20 18:04 跨域配置
 */
@Configuration
@RequiredArgsConstructor
public class CorsConfig implements WebMvcConfigurer {

    @Value("${system.domain}")
    private String domain;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") //允许跨域访问的路径
                .allowedOrigins(domain) //允许跨域访问的源域名
                .allowCredentials(true) //是否发送跨域凭证
                .allowedMethods("*") //允许请求的方法
                .allowedHeaders("*") //允许的请求头
                .maxAge(3600);
    }

}
