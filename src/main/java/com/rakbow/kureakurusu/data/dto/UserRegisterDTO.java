package com.rakbow.kureakurusu.data.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * @author Rakbow
 * @since 2024/3/13 14:27
 */
public record UserRegisterDTO(
        @NotBlank(message = "{user.register.username_required}")
        String username,
        @NotBlank(message = "{user.register.password_required}")
        String password,
        @NotBlank(message = "{user.register.email_required}")
        String email
) {
}
