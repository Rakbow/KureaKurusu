package com.rakbow.kureakurusu.data.system;

import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rakbow
 * @since 2023-08-04 16:14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResult {

    private Cookie cookie;
    private String ticket;
    private LoginUser user;

}
