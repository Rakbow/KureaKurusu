package com.rakbow.kureakurusu.controller;

import com.google.code.kaptcha.Producer;
import com.rakbow.kureakurusu.data.CommonConstant;
import com.rakbow.kureakurusu.data.dto.LoginDto;
import com.rakbow.kureakurusu.data.entity.User;
import com.rakbow.kureakurusu.data.system.ApiResult;
import com.rakbow.kureakurusu.data.system.LoginResult;
import com.rakbow.kureakurusu.service.UserService;
import com.rakbow.kureakurusu.util.I18nHelper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

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

    //注册新用户
    @PostMapping("register")
    public String register(Model model, User user) {
        Map<String, Object> map = userSrv.register(user);
        if (map == null || map.isEmpty()) {
            //操作信息
            model.addAttribute("msg", "注册成功,我们已经向您的邮箱发送了一封激活邮件,请尽快激活!");
            //跳转目标
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/site/register";
        }
    }

    //激活账号
    @GetMapping("activation/{userId}/{code}")
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code) {
        int result = userSrv.activation(userId, code);
        if (result == CommonConstant.ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "激活成功,您的账号已经可以正常使用了!");
            model.addAttribute("target", "/login");
        } else if (result == CommonConstant.ACTIVATION_REPEAT) {
            model.addAttribute("msg", "无效操作,该账号已经激活过了!");
            model.addAttribute("target", "/index");
        } else {
            model.addAttribute("msg", "激活失败,您提供的激活码不正确!");
            model.addAttribute("target", "/index");
        }
        return "/site/operate-result";
    }

    //获取验证码图片
    @RequestMapping(path = "kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session) {
        // 生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        // 将验证码存入session
        session.setAttribute("kaptcha", text);

        // 将图片输出给浏览器
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            log.error("响应验证码失败:" + e.getMessage());
        }
    }

    @PostMapping("login")
    public ApiResult login(@RequestBody LoginDto dto, HttpSession session, HttpServletResponse response) {
        ApiResult res = new ApiResult();
        try {
            // 检查验证码
            String kaptcha = (String) session.getAttribute("kaptcha");
            if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(dto.getVerifyCode()) || !kaptcha.equalsIgnoreCase(dto.getVerifyCode())) {
                res.setErrorMessage(I18nHelper.getMessage("login.verify_code.error"));
                return res;
            }
            //检查是否记住 => 设置缓存时间
            int expiredSeconds = dto.getRememberMe() ? CommonConstant.REMEMBER_EXPIRED_SECONDS : CommonConstant.DEFAULT_EXPIRED_SECONDS;

            //检查账号,密码
            LoginResult loginResult = userSrv.login(dto.getUsername(), dto.getPassword(), expiredSeconds);
            if (StringUtils.isBlank(loginResult.getTicket())) {
                res.fail(loginResult.getError());
                return res;
            } else {
                Cookie cookie = new Cookie("ticket", loginResult.getTicket());
                cookie.setPath(contextPath);
                cookie.setMaxAge(expiredSeconds);
                response.addCookie(cookie);
            }
            res.loadData(loginResult);
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    @PostMapping("logout")
    public ApiResult logout(@CookieValue("ticket") String ticket, HttpServletResponse response) {
        ApiResult res = new ApiResult();
        try {
            //logout
            userSrv.logout(ticket);
            SecurityContextHolder.clearContext();
            //clear cookie
            Cookie cookie = new Cookie("ticket", null);
            cookie.setPath(contextPath);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

}
