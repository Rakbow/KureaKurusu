package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.UserMapper;
import com.rakbow.kureakurusu.data.dto.UserActivationDTO;
import com.rakbow.kureakurusu.data.dto.UserRegisterDTO;
import com.rakbow.kureakurusu.data.entity.User;
import com.rakbow.kureakurusu.exception.ApiException;
import com.rakbow.kureakurusu.toolkit.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Rakbow
 * @since 2022-08-02 0:43
 */
@RequiredArgsConstructor
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    private final UserMapper mapper;
//    private final MailClient mailClient;

    @SneakyThrows
    @Transactional
    public void register(UserRegisterDTO dto) {
        //check username and email duplicate
        long count;
        count = count(new LambdaQueryWrapper<User>().eq(User::getUsername, dto.username()));
        if (count != 0) throw new ApiException("user.register.username_duplicate");
        count = count(new LambdaQueryWrapper<User>().eq(User::getEmail, dto.email()));
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
        User user = getById(dto.userId());
        //check
        if (user.getStatus())
            throw new ApiException("user.activation.duplicate");
        if (!StringUtil.equals(user.getActivationCode(), dto.code()))
            throw new ApiException("user.activation.failure");
        //activation
        mapper.update(new LambdaUpdateWrapper<User>().eq(User::getId, dto.userId()).set(User::getStatus, 1));
    }

}
