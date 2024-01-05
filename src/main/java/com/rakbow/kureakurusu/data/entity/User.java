package com.rakbow.kureakurusu.data.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author Rakbow
 * @since 2022-08-01 18:14
 */
@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String salt;
    private String email;
    private int type;
    private int status;
    private String activationCode;
    private String headerUrl;
    private Date createTime;

}
