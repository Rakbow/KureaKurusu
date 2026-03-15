package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.rakbow.kureakurusu.data.dto.UserRegisterDTO;
import com.rakbow.kureakurusu.toolkit.CommonUtil;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.jackson.BooleanToIntDeserializer;
import lombok.Data;
import tools.jackson.databind.annotation.JsonDeserialize;

/**
 * @author Rakbow
 * @since 2022-08-01 18:14
 */
@Data
@TableName(value = "r1_sys_user", autoResultMap = true)
public class User {

    private Long id;
    private String username;
    private String password;
    private String salt;
    private String email;
    private String activationCode;
    private String avatar;
    private String createdAt;
    @JsonDeserialize(using = BooleanToIntDeserializer.class)
    private Boolean status;

    public User() {
        id = 0L;
        username = "";
        password = "";
        salt = "";
        email = "";
        status = true;
        activationCode = "";
        avatar = "";
        createdAt = DateHelper.nowStr();
    }

    public User(UserRegisterDTO dto) {
        this.username = dto.username();
        this.email = dto.email();
        this.salt = CommonUtil.generateUUID(5);
        this.password = CommonUtil.md5(STR."\{dto.password()}\{this.salt}}");
        this.activationCode = CommonUtil.generateUUID(0);
        //设置用户默认头像
        //user.setHeaderUrl(String.format("", new Random().nextInt(1000)));
    }

}
