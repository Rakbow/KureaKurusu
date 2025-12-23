package com.rakbow.kureakurusu.data.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * @author Rakbow
 * @since 2024/3/8 16:54
 */
public record LoginDTO(
        String verifyCode,
        Boolean rememberMe,
        @NotBlank(message = "{login.username.empty}")
        String username,
        @NotBlank(message = "{login.password.empty}")
        String password
) {
}
