package com.rakbow.kureakurusu.controller;

import com.google.code.kaptcha.Producer;
import com.rakbow.kureakurusu.data.dto.LoginDTO;
import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.common.LoginResult;
import com.rakbow.kureakurusu.service.UserService;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import static com.rakbow.kureakurusu.data.common.Constant.RISK;

/**
 * @author Rakbow
 * @since 2022-08-02 0:20
 */
@RestController
@RequiredArgsConstructor
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private final UserService userSrv;
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
    public ApiResult login(@RequestBody LoginDTO dto, HttpSession session, HttpServletResponse response) throws Exception {
        ApiResult res = new ApiResult();
        //check captcha
        String kaptcha = (String) session.getAttribute("kaptcha");
        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(dto.getVerifyCode()) || !kaptcha.equalsIgnoreCase(dto.getVerifyCode()))
            return res.fail(I18nHelper.getMessage("login.verify_code.error"));
        //login
        LoginResult loginResult = userSrv.login(dto);
        //load cookie to response
        response.addCookie(loginResult.getCookie());
        //return
        HashMap<String, Object> data = new HashMap<>();
        data.put("token", loginResult.getTicket());
        data.put("user", loginResult.getUser());
        res.loadData(data);
        return res;
    }

    @PostMapping("logout")
    public ApiResult logout(@CookieValue("ticket") String ticket, HttpServletResponse response) {
        //logout
        userSrv.logout(ticket);
        SecurityContextHolder.clearContext();
        //clear cookie
        Cookie cookie = new Cookie("ticket", null);
        cookie.setPath(contextPath);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return new ApiResult();
    }

}
