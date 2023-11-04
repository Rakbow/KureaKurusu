package com.rakbow.kureakurusu.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2022-08-17 0:22
 * @Description:
 */
@Data
public class LoginTicket {

    private Integer id;
    private int userId;
    private String ticket;
    private int status;
    private Date expired;

    public LoginTicket() {
        id = 0;
        userId = 0;
        ticket = "";
        status = 0;
    }

}
