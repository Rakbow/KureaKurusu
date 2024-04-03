package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.LoginTicketMapper;
import com.rakbow.kureakurusu.dao.UserMapper;
import com.rakbow.kureakurusu.data.common.LoginResult;
import com.rakbow.kureakurusu.data.common.LoginUser;
import com.rakbow.kureakurusu.data.dto.LoginDTO;
import com.rakbow.kureakurusu.data.dto.UserActivationDTO;
import com.rakbow.kureakurusu.data.dto.UserRegisterDTO;
import com.rakbow.kureakurusu.data.entity.LoginTicket;
import com.rakbow.kureakurusu.data.entity.User;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.CommonUtil;
import com.rakbow.kureakurusu.util.common.RedisUtil;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.rakbow.kureakurusu.data.common.Constant.*;

/**
 * @author Rakbow
 * @since 2022-08-02 0:43
 */
@RequiredArgsConstructor
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    private final UserMapper mapper;
//    private final MailClient mailClient;
    private final LoginTicketMapper loginTicketMapper;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    private final RedisUtil redisUtil;

    @SneakyThrows
    @Transactional
    public void register(UserRegisterDTO dto) {
        //check username and email duplicate
        long count;
        count = count(new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername()));
        if (count != 0) throw new Exception(I18nHelper.getMessage("user.register.username_duplicate"));
        count = count(new LambdaQueryWrapper<User>().eq(User::getEmail, dto.getEmail()));
        if (count != 0) throw new Exception(I18nHelper.getMessage("user.register.email_duplicate"));
        //register new user
        User user = new User(dto);
        //save
        save(user);
        //activate email
        // Context context = new Context();
        // context.setVariable("username", user.getUsername());
        // String activationUrl = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        // context.setVariable("activationUrl", activationUrl);
        // String content = templateEngine.process("/mail/activation", context);
        // mailClient.sendMail(user.getEmail(), "激活账号", content);
    }

    @SneakyThrows
    @Transactional
    public void activation(UserActivationDTO dto) {
        User user = getById(dto.getUserId());
        //check
        if (user.getStatus())
            throw new Exception(I18nHelper.getMessage("user.activation.duplicate"));
        if (!StringUtils.equals(user.getActivationCode(), dto.getCode()))
            throw new Exception(I18nHelper.getMessage("user.activation.failure"));
        //activation
        mapper.update(new LambdaUpdateWrapper<User>().eq(User::getId, dto.getUserId()).set(User::getStatus, 1));
    }

    @SneakyThrows
    @Transactional
    public LoginResult login(LoginDTO dto) throws Exception {

        //check empty
        if (StringUtils.isBlank(dto.getUsername())) throw new Exception(I18nHelper.getMessage("login.username.empty"));
        if (StringUtils.isBlank(dto.getPassword())) throw new Exception(I18nHelper.getMessage("login.password.empty"));
        //check user exist
        User user = getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername()));
        if (user == null) throw new Exception(I18nHelper.getMessage("login.user.not_exist"));
        //check user activation status
        if (!user.getStatus()) throw new Exception(I18nHelper.getMessage("login.user.inactivated"));
        //check user password
        String password = CommonUtil.md5(STR."\{dto.getPassword()}\{user.getSalt()}");
        if (!user.getPassword().equals(password)) throw new Exception(I18nHelper.getMessage("login.password.error"));

        //generate login ticket
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommonUtil.generateUUID());
        int expiredSeconds = dto.getRememberMe() ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS * 1000;
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000L));
        loginTicketMapper.insert(loginTicket);

        //save ticket to redis
        redisUtil.set(STR."login_ticket\{RISK}\{loginTicket.getTicket()}", loginTicket.getUserId(), expiredSeconds * 1000L);

        //generate cookie
        Cookie cookie = new Cookie("ticket", loginTicket.getTicket());
        cookie.setPath(contextPath);
        cookie.setMaxAge(expiredSeconds);

        //generate loginUser
        LoginUser loginUser = new LoginUser();
        loginUser.create(user);

        //generate login result

        return LoginResult.builder()
                .user(loginUser)
                .cookie(cookie)
                .ticket(loginTicket.getTicket())
                .build();
    }

    @SneakyThrows
    @Transactional
    public void logout(String ticket) {
        //delete login ticket from redis
        String redisTicketKey = STR."login_ticket\{RISK}\{ticket}";
        if (redisUtil.hasKey(redisTicketKey)) redisUtil.delete(redisTicketKey);
        //update login ticket from db
        loginTicketMapper.update(
                new LambdaUpdateWrapper<LoginTicket>()
                        .eq(LoginTicket::getTicket, ticket)
                        .set(LoginTicket::getStatus, 1)
        );
    }

}
