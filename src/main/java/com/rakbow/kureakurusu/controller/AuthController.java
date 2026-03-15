package com.rakbow.kureakurusu.controller;

import com.google.code.kaptcha.Producer;
import com.rakbow.kureakurusu.data.auth.LoginUser;
import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.LoginDTO;
import com.rakbow.kureakurusu.service.AuthService;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import static com.rakbow.kureakurusu.data.common.Constant.RISK;

/**
 * @author Rakbow
 * @since 2022-08-02 0:20
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {

    private final AuthService srv;
    private final Producer kaptchaProducer;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    //get captcha image
    @GetMapping("kaptcha")
    public void getKaptcha(HttpServletResponse response, HttpSession session) {
        //generate captcha text and image
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);
        //save to session
        session.setAttribute("kaptcha", text);
        //save image to browser
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            log.error(STR."\{I18nHelper.getMessage("system.captcha.failed")}\{RISK}}\{e.getMessage()}");
        }
    }

    @PostMapping("login")
    public ApiResult login(@Valid @RequestBody LoginDTO dto, HttpSession session,
                           HttpServletRequest request, HttpServletResponse response, BindingResult errors) {
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        LoginUser res = srv.login(dto, request, session);
        //generate cookie
        Cookie cookie = new Cookie("ticket", res.getTicket());
        cookie.setHttpOnly(true);
        cookie.setPath(contextPath);
        cookie.setMaxAge(res.getExpires());
        //load cookie to response
        response.addCookie(cookie);
        return ApiResult.ok(res);
    }

    @PostMapping("logout")
    public ApiResult logout(@CookieValue(value = "ticket", required = false) String ticket,
                            HttpSession session,
                            HttpServletResponse response,
                            HttpServletRequest request) {
        //logout
        srv.logout(ticket);
        //clear Spring Security context
        SecurityContextHolder.clearContext();
        //clear session
        session.invalidate();
        //clear cookie
        Cookie cookie = new Cookie("ticket", null);
        cookie.setHttpOnly(true);
        cookie.setPath(contextPath);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ApiResult.ok();
    }

}
