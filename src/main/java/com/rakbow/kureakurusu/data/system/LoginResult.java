package com.rakbow.kureakurusu.data.system;

import lombok.Data;

/**
 * @author Rakbow
 * @since 2023-08-04 16:14
 */
@Data
public class LoginResult {

    private String error;
    private String ticket;
    private LoginUser user;

    public LoginResult() {
        error = "";
        ticket = "";
        user = new LoginUser();
    }

}
