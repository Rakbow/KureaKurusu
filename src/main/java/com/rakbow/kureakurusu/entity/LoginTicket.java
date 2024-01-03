package com.rakbow.kureakurusu.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author Rakbow
 * @since 2022-08-17 0:22
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
