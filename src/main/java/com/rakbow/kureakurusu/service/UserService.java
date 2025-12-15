package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.LoginTicketMapper;
import com.rakbow.kureakurusu.dao.UserMapper;
import com.rakbow.kureakurusu.data.common.LoginResult;
import com.rakbow.kureakurusu.data.common.UserMiniVO;
import com.rakbow.kureakurusu.data.dto.LoginDTO;
import com.rakbow.kureakurusu.data.dto.UserActivationDTO;
import com.rakbow.kureakurusu.data.dto.UserRegisterDTO;
import com.rakbow.kureakurusu.data.entity.LoginTicket;
import com.rakbow.kureakurusu.data.entity.User;
import com.rakbow.kureakurusu.exception.ApiException;
import com.rakbow.kureakurusu.toolkit.CommonUtil;
import com.rakbow.kureakurusu.toolkit.RedisUtil;
import com.rakbow.kureakurusu.toolkit.StringUtil;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
    private final RedisUtil redisUtil;

    private final Converter converter;

    @SneakyThrows
    @Transactional
    public void register(UserRegisterDTO dto) {
        //check username and email duplicate
        long count;
        count = count(new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername()));
        if (count != 0) throw new ApiException("user.register.username_duplicate");
        count = count(new LambdaQueryWrapper<User>().eq(User::getEmail, dto.getEmail()));
        if (count != 0) throw new ApiException("user.register.email_duplicate");
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
            throw new ApiException("user.activation.duplicate");
        if (!StringUtil.equals(user.getActivationCode(), dto.getCode()))
            throw new ApiException("user.activation.failure");
        //activation
        mapper.update(new LambdaUpdateWrapper<User>().eq(User::getId, dto.getUserId()).set(User::getStatus, 1));
    }

    @SneakyThrows
    @Transactional
    public LoginResult login(LoginDTO dto, String kaptcha) {

        //check captcha
        if (StringUtil.isBlank(kaptcha) || StringUtil.isBlank(dto.getVerifyCode())
                || !kaptcha.equalsIgnoreCase(dto.getVerifyCode()))
            throw new ApiException("login.verify_code.error");

        //check user exist
        User user = getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername()));
        if (user == null) throw new ApiException("login.user.not_exist");
        //check user activation status
        if (!user.getStatus()) throw new ApiException("login.user.inactivated");
        //check user password
        String password = CommonUtil.md5(STR."\{dto.getPassword()}\{user.getSalt()}");
        if (!user.getPassword().equals(password)) throw new ApiException("login.password.error");
        //count expired time
        int expires = (dto.getRememberMe() ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS) * 1000;
        //generate login ticket
        LoginTicket loginTicket = LoginTicket.builder()
                .uid(user.getId())
                .ticket(CommonUtil.generateUUID(0))
                .expired(new Date(System.currentTimeMillis() + expires ))
                .build();
        loginTicketMapper.insert(loginTicket);

        //save ticket to redis
        redisUtil.set(STR."login_ticket\{RISK}\{loginTicket.getTicket()}", loginTicket.getUid(), expires);

        //generate loginUser
        UserMiniVO userMiniVO = converter.convert(user, UserMiniVO.class);

        //generate login result
        return LoginResult.builder()
                .user(userMiniVO)
                .ticket(loginTicket.getTicket())
                .expires(expires)
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
