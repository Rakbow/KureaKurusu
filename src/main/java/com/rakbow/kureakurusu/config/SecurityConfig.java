package com.rakbow.kureakurusu.config;

import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.interceptor.TokenAuthFilter;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import com.rakbow.kureakurusu.toolkit.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

/**
 * @author Rakbow
 * @since 2022-09-27 0:12
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenAuthFilter tokenFilter;

    @Bean
    public HttpFirewall httpFirewall() {
        return new DefaultHttpFirewall();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/resources/**");
    }

    @SneakyThrows
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) {
        return http
                .authorizeHttpRequests(auth -> auth
                        //部分接口绕开登录校验
                        .requestMatchers(
                                "/auth/login",
                                "/auth/logout",
                                "/auth/kaptcha"
                        ).permitAll()
                        //其余接口需登录校验
                        .anyRequest().authenticated()
                )
                //前后端分离项目, 启用跨域支持
                .cors(Customizer.withDefaults())
                //前后端分离（JWT / Token）一般不用 CSRF
                .csrf(AbstractHttpConfigurer::disable)
                //关闭浏览器弹窗认证（用户名密码弹窗）
                .httpBasic(AbstractHttpConfigurer::disable)
                //禁用 Session
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .logout(AbstractHttpConfigurer::disable)
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
                //异常处理
                .exceptionHandling(auth -> auth
                        //未登录
                        .authenticationEntryPoint((_, resp, _) -> {
                            resp.setStatus(HttpStatus.UNAUTHORIZED.value());
                            resp.setContentType("application/json;charset=utf-8");
                            resp.getWriter().write(JsonUtil.toJson(ApiResult.fail(I18nHelper.getMessage("auth.not_login"))));
                        })
                        //权限不足
                        .accessDeniedHandler((_, resp, _) -> {
                            resp.setStatus(HttpStatus.FORBIDDEN.value());
                            resp.setContentType("application/json;charset=utf-8");
                            resp.getWriter().write(JsonUtil.toJson(ApiResult.fail(I18nHelper.getMessage("auth.not_authority"))));
                        })
                ).build();
    }

}
