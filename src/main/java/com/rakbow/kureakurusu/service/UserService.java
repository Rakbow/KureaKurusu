package com.rakbow.kureakurusu.service;

import com.alibaba.fastjson2.JSON;
import com.rakbow.kureakurusu.dao.LoginTicketMapper;
import com.rakbow.kureakurusu.dao.UserMapper;
import com.rakbow.kureakurusu.data.emun.system.UserAuthority;
import com.rakbow.kureakurusu.data.system.LoginResult;
import com.rakbow.kureakurusu.data.system.LoginUser;
import com.rakbow.kureakurusu.entity.LoginTicket;
import com.rakbow.kureakurusu.entity.User;
import com.rakbow.kureakurusu.util.common.CookieUtil;
import com.rakbow.kureakurusu.data.ActionResult;
import com.rakbow.kureakurusu.data.ApiInfo;
import com.rakbow.kureakurusu.data.CommonConstant;
import com.rakbow.kureakurusu.util.common.CommonUtil;
import com.rakbow.kureakurusu.util.common.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2022-08-02 0:43
 * @Description:
 */
@Service
public class UserService{

    @Resource
    private UserMapper userMapper;

    @Resource
    private MailClient mailClient;

    @Resource
    private LoginTicketMapper loginTicketMapper;

    @Value("${kureakurusu.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Transactional( readOnly = true )
    public User findUserById(int id) {
        return userMapper.selectUserById(id);
    }

    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空!");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空!");
            return map;
        }

        // 验证账号
        User u = userMapper.selectUserByUsername(user.getUsername());
        if (u != null) {
            map.put("usernameMsg", "该账号已存在!");
            return map;
        }

        // 验证邮箱
        u = userMapper.selectUserByEmail(user.getEmail());
        if (u != null) {
            map.put("emailMsg", "该邮箱已被注册!");
            return map;
        }

        // 注册用户
        user.setSalt(CommonUtil.generateUUID().substring(0, 5));
        user.setPassword(CommonUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(1);//设置用户类型 1-普通用户
        user.setStatus(1);
        user.setActivationCode(CommonUtil.generateUUID());
        //设置用户默认头像
        //user.setHeaderUrl(String.format("", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        // 激活邮件
        // Context context = new Context();
        // context.setVariable("username", user.getUsername());
        // String activationUrl = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        // context.setVariable("activationUrl", activationUrl);
        // String content = templateEngine.process("/mail/activation", context);
        // mailClient.sendMail(user.getEmail(), "激活账号", content);

        return map;
    }

    public int activation(int userId, String code) {
        User user = userMapper.selectUserById(userId);
        if (user.getStatus() == 1) {
            return CommonConstant.ACTIVATION_REPEAT;
        } else if (user.getActivationCode().equals(code)) {
            userMapper.updateStatus(userId, 1);
            return CommonConstant.ACTIVATION_SUCCESS;
        } else {
            return CommonConstant.ACTIVATION_FAILURE;
        }
    }

    //登录
    public LoginResult login(String username, String password, int expiredSeconds) {
        LoginResult res = new LoginResult();
        // 空值处理
        if (StringUtils.isBlank(username)) {
            res.setError(ApiInfo.USERNAME_ARE_EMPTY);
            return res;
        }
        if (StringUtils.isBlank(password)) {
            res.setError(ApiInfo.PASSWORD_ARE_EMPTY);
            return res;
        }

        // 验证账号
        User user = userMapper.selectUserByUsername(username);
        if (user == null) {
            res.setError(ApiInfo.USER_NOT_EXIST);
            return res;
        }

        // 验证状态
        if (user.getStatus() == 0) {
            res.setError(ApiInfo.USER_ARE_INACTIVATED);
            return res;
        }

        // 验证密码
        password = CommonUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            res.setError(ApiInfo.INCORRECT_PASSWORD);
            return res;
        }

        // 生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommonUtil.generateUUID());
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000L));
        loginTicketMapper.insertLoginTicket(loginTicket);

        res.setTicket(loginTicket.getTicket());

        LoginUser loginUser = new LoginUser();
        loginUser.create(user);

        res.setUser(loginUser);

        return res;
    }

    public void logout(String ticket) {
        loginTicketMapper.updateStatus(ticket, 1);
    }

    public LoginTicket findLoginTicket(String ticket) {
        return loginTicketMapper.selectByTicket(ticket);
    }

    public void updateHeader(int userId, String headerUrl) {
        userMapper.updateHeader(userId, headerUrl);
    }

    //配置用户权限!!!
    public Collection<? extends GrantedAuthority> getAuthorities(int userId) {
        User user = this.findUserById(userId);

        List<GrantedAuthority> list = new ArrayList<>();

        switch (user.getType()) {
            case 0 -> list.add(new SimpleGrantedAuthority("ROLE_" + UserAuthority.ADMIN.getNameEn()));
            case 1 -> list.add(new SimpleGrantedAuthority("ROLE_" + UserAuthority.USER.getNameEn()));
            case 2 -> list.add(new SimpleGrantedAuthority("ROLE_" + UserAuthority.JUNIOR_EDITOR.getNameEn()));
            case 3 -> list.add(new SimpleGrantedAuthority("ROLE_" + UserAuthority.SENIOR_EDITOR.getNameEn()));
        }

        return list;
    }

    public ActionResult checkAuthority(HttpServletRequest request){
        ActionResult res = new ActionResult();
        // 从cookie中获取凭证
        String ticket = CookieUtil.getValue(request, "ticket");

        if (ticket != null) {
            // 查询凭证
            LoginTicket loginTicket = findLoginTicket(ticket);
            // 检查凭证是否有效
            if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                // 根据凭证查询用户
                User user = findUserById(loginTicket.getUserId());
                if (user.getType() == 1) {
                    res.setErrorMessage(ApiInfo.NOT_AUTHORITY);
                }
            }else {
                res.setErrorMessage(ApiInfo.NOT_LOGIN);
            }
        } else {
            res.setErrorMessage(ApiInfo.NOT_LOGIN);
        }
        return res;
    }

    public User getUserByRequest(HttpServletRequest request) {
        // 从cookie中获取凭证
        String ticket = CookieUtil.getValue(request, "ticket");

        if (ticket != null) {
            // 查询凭证
            LoginTicket loginTicket = findLoginTicket(ticket);
            // 检查凭证是否有效
            if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                // 根据凭证查询用户
                return findUserById(loginTicket.getUserId());
            }
        }
        return null;
    }

}
