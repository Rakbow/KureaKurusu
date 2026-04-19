package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rakbow.kureakurusu.dao.CommonMapper;
import com.rakbow.kureakurusu.dao.LoginTicketMapper;
import com.rakbow.kureakurusu.data.auth.LoginUser;
import com.rakbow.kureakurusu.data.constant.RedisKey;
import com.rakbow.kureakurusu.data.dto.LoginDTO;
import com.rakbow.kureakurusu.data.entity.LoginTicket;
import com.rakbow.kureakurusu.data.entity.User;
import com.rakbow.kureakurusu.exception.ApiException;
import com.rakbow.kureakurusu.toolkit.CommonUtil;
import com.rakbow.kureakurusu.toolkit.RedisUtil;
import com.rakbow.kureakurusu.toolkit.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.rakbow.kureakurusu.data.common.Constant.DEFAULT_EXPIRED_SECONDS;
import static com.rakbow.kureakurusu.data.common.Constant.REMEMBER_EXPIRED_SECONDS;

/**
 * @author Rakbow
 * @since 2026/3/15 1:50
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final LoginTicketMapper loginTicketMapper;
    private final CommonMapper commonMapper;
    private final RedisUtil redisUtil;
    private final UserService userSrv;

    @SneakyThrows
    @Transactional
    public LoginUser login(LoginDTO dto, HttpServletRequest request, HttpSession session) {

        //get captcha from http session
        String kaptcha = session.getAttribute("kaptcha").toString();
        //get ip address from http request
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtil.isBlank(ip)) ip = request.getRemoteAddr();
        //get user agent from http request
        String userAgent = request.getHeader("User-Agent");

        //check captcha
        if (StringUtil.isBlank(kaptcha) || StringUtil.isBlank(dto.verifyCode())
                || !kaptcha.equalsIgnoreCase(dto.verifyCode()))
            throw new ApiException("login.verify_code.error");

        //check user exist
        User user = userSrv.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, dto.username()));
        if (user == null) throw new ApiException("login.user.not_exist");
        //check user activation status
        if (!user.getStatus()) throw new ApiException("login.user.inactivated");
        //check user password
        String password = CommonUtil.md5(STR."\{dto.password()}\{user.getSalt()}");
        if (!StringUtil.equals(user.getPassword(), password)) throw new ApiException("login.password.error");
        //count expired time
        int expires = (dto.rememberMe() ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS);

        //generate login ticket
        LoginTicket ticket = LoginTicket.builder()
                .ticket(CommonUtil.generateUUID(0))
                .uid(user.getId())
                .ipAddress(ip)
                .agent(userAgent)
                .expired(new Date(System.currentTimeMillis() + expires * 1000 ))
                .build();
        loginTicketMapper.insert(ticket);

        LoginUser res = commonMapper.getLoginUser(user.getId());
        res.setIpAddress(ip);
        res.setAgent(userAgent);
        res.setTicket(ticket.getTicket());
        res.setExpires(expires);
        //save ticket to redis
        redisUtil.set(STR."\{RedisKey.LOGIN_TICKET}\{ticket.getTicket()}", res, expires);



        return res;
    }

    @SneakyThrows
    @Transactional
    public void logout(String ticket) {
        //delete login ticket from redis
        String key = STR."\{RedisKey.LOGIN_TICKET}\{ticket}";
        if (redisUtil.hasKey(key)) redisUtil.delete(key);
    }

}
