package com.rakbow.kureakurusu.data.dto;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "{login.username.empty}")
    private String username;
    @NotBlank(message = "{login.password.empty}")
    private String password;

}
