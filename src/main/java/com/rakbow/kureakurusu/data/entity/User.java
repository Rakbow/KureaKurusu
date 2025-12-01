package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rakbow.kureakurusu.data.common.UserMiniVO;
import com.rakbow.kureakurusu.data.dto.UserRegisterDTO;
import com.rakbow.kureakurusu.data.enums.UserAuthority;
import com.rakbow.kureakurusu.toolkit.CommonUtil;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.convert.GlobalConverters;
import com.rakbow.kureakurusu.toolkit.jackson.BooleanToIntDeserializer;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2022-08-01 18:14
 */
@Data
@TableName(value = "user", autoResultMap = true)
@AutoMapper(target = UserMiniVO.class, reverseConvertGenerate = false, uses = GlobalConverters.class)
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
    private String avatar;
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
        avatar = "";
        createDate = DateHelper.nowStr();
    }

    public User(UserRegisterDTO dto) {
        this.username = dto.getUsername();
        this.email = dto.getEmail();
        this.salt = CommonUtil.generateUUID(5);
        this.password = CommonUtil.md5(STR."\{dto.getPassword()}\{this.salt}}");
        this.type = UserAuthority.USER;
        this.activationCode = CommonUtil.generateUUID(0);
        //设置用户默认头像
        //user.setHeaderUrl(String.format("", new Random().nextInt(1000)));
    }

}
