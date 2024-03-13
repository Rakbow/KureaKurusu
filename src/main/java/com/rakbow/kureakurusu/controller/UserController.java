package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.data.dto.user.UserActivationDTO;
import com.rakbow.kureakurusu.data.dto.user.UserRegisterDTO;
import com.rakbow.kureakurusu.data.system.ApiResult;
import com.rakbow.kureakurusu.service.UserService;
import com.rakbow.kureakurusu.util.I18nHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService srv;

    //register new user
    @PostMapping("register")
    public ApiResult register(@Valid @RequestBody UserRegisterDTO dto, BindingResult errors) {
        ApiResult res = new ApiResult();
        try {
            //check
            if (errors.hasErrors()) return res.fail(errors);
            //register
            srv.register(dto);
            res.ok(I18nHelper.getMessage("user.register.success"));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    //activation user
    @PostMapping("activation")
    public ApiResult activation(@RequestBody UserActivationDTO dto) {
        ApiResult res = new ApiResult();
        try {
            srv.activation(dto);
            res.ok(I18nHelper.getMessage("user.activation.success"));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

}
