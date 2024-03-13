package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.dto.base.DTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/3/8 16:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginDTO extends DTO {

    private String verifyCode;
    private Boolean rememberMe;
    private String username;
    private String password;

}
