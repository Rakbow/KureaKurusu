package com.rakbow.kureakurusu.data.common;

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

    private UserMiniVO user;
    private String ticket;
    private int expires;

}
