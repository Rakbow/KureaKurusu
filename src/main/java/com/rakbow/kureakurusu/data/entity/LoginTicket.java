package com.rakbow.kureakurusu.data.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import tools.jackson.databind.annotation.JsonDeserialize;
import com.rakbow.kureakurusu.toolkit.jackson.BooleanToIntDeserializer;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Rakbow
 * @since 2022-08-17 0:22
 */
@Data
@Builder
@TableName(value = "r1_sys_login_ticket", autoResultMap = true)
public class LoginTicket {

    private Long id;
    private String ticket;
    private long uid;
    private String ipAddress;
    private String agent;
    private Date expired;
    private Timestamp createdAt;
    @JsonDeserialize(using = BooleanToIntDeserializer.class)
    private Boolean status;
}
