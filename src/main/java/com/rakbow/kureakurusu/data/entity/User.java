package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rakbow.kureakurusu.data.dto.UserRegisterDTO;
import com.rakbow.kureakurusu.data.emun.UserAuthority;
import com.rakbow.kureakurusu.toolkit.CommonUtil;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.jackson.BooleanToIntDeserializer;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2022-08-01 18:14
 */
@Data
@TableName(value = "user", autoResultMap = true)
public class User {

    private Long id;
    private String username;
    private String password;
    private String salt;
    private String email;
    private UserAuthority type;
    @JsonDeserialize(using = BooleanToIntDeserializer.class)
    private Boolean status;
    private String activationCode;
    private String headerUrl;
    private String createDate;

    public User() {
        id = 0L;
        username = "";
        password = "";
        salt = "";
        email = "";
        type = UserAuthority.USER;
        status = true;
        activationCode = "";
        headerUrl = "";
        createDate = DateHelper.nowStr();
    }

    public User(UserRegisterDTO dto) {
        this.username = dto.getUsername();
        this.email = dto.getEmail();
        this.salt = CommonUtil.generateUUID().substring(0, 5);
        this.salt = CommonUtil.generateUUID().substring(0, 5);
        this.password = CommonUtil.md5(STR."\{dto.getPassword()}\{this.salt}}");
        this.type = UserAuthority.USER;
        this.activationCode = CommonUtil.generateUUID();
        //设置用户默认头像
        //user.setHeaderUrl(String.format("", new Random().nextInt(1000)));
    }

}
