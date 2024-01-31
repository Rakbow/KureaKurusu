package com.rakbow.kureakurusu.config;

import com.rakbow.kureakurusu.data.system.ApiResult;
import com.rakbow.kureakurusu.data.emun.system.UserAuthority;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.JsonUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

import java.io.PrintWriter;

/**
 * @author Rakbow
 * @since 2022-09-27 0:12
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public HttpFirewall httpFirewall() {
        return new DefaultHttpFirewall();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/resources/**");
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        // .requestMatchers(
                        //     //需要登陆权限
                        //     "/user/setting",
                        //     "/user/upload"
                        // )
                        // .hasAnyAuthority(
                        //         "ROLE_" + UserAuthority.ADMIN.getNameEn(),
                        //         "ROLE_" + UserAuthority.JUNIOR_EDITOR.getNameEn(),
                        //         "ROLE_" + UserAuthority.SENIOR_EDITOR.getNameEn(),
                        //         "ROLE_" + UserAuthority.USER.getNameEn()
                        // )
                        // .requestMatchers(
                        //         "/db/update-item-detail",
                        //         "/db/update-item-bonus"
                        // )
                        // .hasAnyAuthority(
                        //         "ROLE_" + UserAuthority.ADMIN.getNameEn(),
                        //         "ROLE_" + UserAuthority.SENIOR_EDITOR.getNameEn(),
                        //         "ROLE_" + UserAuthority.JUNIOR_EDITOR.getNameEn()
                        // )
                        // .requestMatchers(
                        //         "/db/franchise/add",
                        //         "/db/franchise/delete",
                        //         "/db/franchise/update"
                        // )
                        // .hasAnyAuthority(
                        //         "ROLE_" + UserAuthority.ADMIN.getNameEn(),
                        //         "ROLE_" + UserAuthority.SENIOR_EDITOR.getNameEn()
                        // )
                        .anyRequest()
                        .permitAll()
                )
                //禁用跨域访问限制
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                // 清除session
                .logout(logout -> logout.clearAuthentication(true).invalidateHttpSession(true))

                //权限不足时
                .exceptionHandling(auth -> auth
                                .authenticationEntryPoint((request, response, e) -> {
                                    String xRequestedWith = request.getHeader("x-requested-with");
                                    //识别请求为同步请求还是异步请求
                                    if ("XMLHttpRequest".equals(xRequestedWith)) {
                                        //异步请求
                                        response.setContentType("application/plain;charset=utf-8");
                                        PrintWriter writer = response.getWriter();
                                        writer.write(JsonUtil.toJson(new ApiResult(0, I18nHelper.getMessage("auth.not_login"))));
                                    } else {
                                        //同步请求
                                        response.sendRedirect("/login");
                                    }
                                })
                                .accessDeniedHandler((request, response, e) -> {
                                String xRequestedWith = request.getHeader("x-requested-with");
                                if ("XMLHttpRequest".equals(xRequestedWith)) {
                                    response.setContentType("application/plain;charset=utf-8");
                                    PrintWriter writer = response.getWriter();
                                    writer.write(JsonUtil.toJson(new ApiResult(0, I18nHelper.getMessage("auth.not_authority"))));
                                } else {
                                    response.sendRedirect("/denied");
                                }
                                })
                ).build();
    }

}
