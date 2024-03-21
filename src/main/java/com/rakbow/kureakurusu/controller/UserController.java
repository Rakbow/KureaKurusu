package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.data.dto.UserActivationDTO;
import com.rakbow.kureakurusu.data.dto.UserRegisterDTO;
import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.service.UserService;
import com.rakbow.kureakurusu.util.I18nHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rakbow
 * @since 2022-08-20 18:36
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {

    private final UserService srv;

    //register new user
    @PostMapping("register")
    public ApiResult register(@Valid @RequestBody UserRegisterDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //register
        srv.register(dto);
        return new ApiResult().ok(I18nHelper.getMessage("user.register.success"));
    }

    //activation user
    @PostMapping("activation")
    public ApiResult activation(@RequestBody UserActivationDTO dto) {
        srv.activation(dto);
        return new ApiResult().ok(I18nHelper.getMessage("user.activation.success"));
    }

}
