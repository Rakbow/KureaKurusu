package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rakbow.kureakurusu.util.jackson.BooleanToIntDeserializer;
import lombok.Data;

import java.util.Date;

/**
 * @author Rakbow
 * @since 2022-08-17 0:22
 */
@Data
@TableName(value = "login_ticket", autoResultMap = true)
public class LoginTicket {

    private Long id;
    private long userId;
    private String ticket;
    @JsonDeserialize(using = BooleanToIntDeserializer.class)
    private Boolean status;
    private Date expired;

    public LoginTicket() {
        id = 0L;
        userId = 0L;
        ticket = "";
        status = false;
    }

}
