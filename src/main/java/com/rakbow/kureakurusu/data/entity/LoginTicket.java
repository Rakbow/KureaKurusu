package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rakbow.kureakurusu.toolkit.jackson.BooleanToIntDeserializer;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author Rakbow
 * @since 2022-08-17 0:22
 */
@Data
@Builder
@TableName(value = "login_ticket", autoResultMap = true)
public class LoginTicket {

    private Long id;
    private long uid;
    private String ticket;
    @JsonDeserialize(using = BooleanToIntDeserializer.class)
    private Boolean status;
    private Date expired;
}
