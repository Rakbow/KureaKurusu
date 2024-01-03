package com.rakbow.kureakurusu.controller;

import com.alibaba.fastjson2.JSONObject;
import com.google.code.kaptcha.Producer;
import com.rakbow.kureakurusu.data.ApiResult;
import com.rakbow.kureakurusu.data.system.LoginResult;
import com.rakbow.kureakurusu.entity.User;
import com.rakbow.kureakurusu.service.UserService;
import com.rakbow.kureakurusu.data.CommonConstant;
import com.rakbow.kureakurusu.util.I18nHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
// import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author Rakbow
 * @since 2022-08-02 0:20
 */
@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private UserService userService;
    @Resource
    private Producer kaptchaProducer;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    

    //获取注册页面
    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegisterPage() {
        return "/site/register";
    }

    //获取登陆页面
    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLoginPage() {
        return "/site/login";
    }

    //注册新用户
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(Model model, User user) {
        Map<String, Object> map = userService.register(user);
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
    @RequestMapping(path = "/activation/{userId}/{code}", method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code) {
        int result = userService.activation(userId, code);
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
    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session) {
        // 生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        // 将验证码存入session
        session.setAttribute("kaptcha", text);

        // 将突图片输出给浏览器
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            logger.error("响应验证码失败:" + e.getMessage());
        }
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    @ResponseBody
    public String login(@RequestBody JSONObject json, HttpSession session, HttpServletResponse response) {
        ApiResult res = new ApiResult();
        try {
            String verifyCode = json.getString("verifyCode");
            // 检查验证码
            String kaptcha = (String) session.getAttribute("kaptcha");
            if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(verifyCode) || !kaptcha.equalsIgnoreCase(verifyCode)) {
                res.setErrorMessage(I18nHelper.getMessage("login.verify_code.error"));
                return res.toJson();
            }

            //检查是否记住 => 设置缓存时间
            boolean rememberMe = json.getBoolean("rememberMe");
            int expiredSeconds = rememberMe ? CommonConstant.REMEMBER_EXPIRED_SECONDS : CommonConstant.DEFAULT_EXPIRED_SECONDS;

            String username = json.getString("username");
            String password = json.getString("password");

            //// 检查账号,密码
            LoginResult loginResult = userService.login(username, password, expiredSeconds);
            if (loginResult.getTicket() == null) {
                res.setErrorMessage(loginResult.getError());
                return res.toJson();
            } else {
                Cookie cookie = new Cookie("ticket", loginResult.getTicket());
                cookie.setPath(contextPath);
                cookie.setMaxAge(expiredSeconds);
                response.addCookie(cookie);
            }

            JSONObject result = new JSONObject();
            result.put("user", loginResult.getUser());
            result.put("token", loginResult.getTicket());
            res.data = result;

        } catch (Exception e) {
            res.setErrorMessage(e.getMessage());
        }
        return res.toJson();
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public String logout(@CookieValue("ticket") String ticket, HttpServletResponse response) {
        ApiResult res = new ApiResult();
        try {
            userService.logout(ticket);
            SecurityContextHolder.clearContext();

            //清楚cookie
            Cookie cookie = new Cookie("ticket", null);
            cookie.setPath(contextPath);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        } catch (Exception e) {
            res.setErrorMessage(e.getMessage());
        }
        return res.toJson();
    }


}
