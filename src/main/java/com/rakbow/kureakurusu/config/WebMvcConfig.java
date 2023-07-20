package com.rakbow.kureakurusu.config;

import com.rakbow.kureakurusu.controller.interceptor.LoginTicketInterceptor;
import com.rakbow.kureakurusu.controller.interceptor.LocaleResolver;
import com.rakbow.kureakurusu.controller.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.annotation.Resource;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2022-08-17 23:53
 * @Description:
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private LoginTicketInterceptor loginTicketInterceptor;
    @Resource
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
        registry.addInterceptor(tokenInterceptor);
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg", "/**/*.ico", "/**/*.cur");
    }

    @Bean
    public org.springframework.web.servlet.LocaleResolver localeResolver(){
        //添加自己重写的CookieLocaleResolver
        return new LocaleResolver();
    }
    //配置拦截器获取URL中的key=“lang” （?lang=zh_CN）
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

}
