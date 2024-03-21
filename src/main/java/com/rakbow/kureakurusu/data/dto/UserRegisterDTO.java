package com.rakbow.kureakurusu.data.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/3/13 14:27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserRegisterDTO extends DTO {

    @NotBlank(message = "{user.register.username_required}")
    private String username;
    @NotBlank(message = "{user.register.password_required}")
    private String password;
    @NotBlank(message = "{user.register.email_required}")
    private String email;

}
