package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.RoleListQueryDTO;
import com.rakbow.kureakurusu.data.entity.Role;
import com.rakbow.kureakurusu.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rakbow
 * @since 2025/6/22 14:47
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("db/role")
public class RoleController {

    private final RoleService srv;

    @PostMapping("list")
    public ApiResult list(@RequestBody RoleListQueryDTO dto) {
        return new ApiResult().load(srv.list(dto));
    }

    @PostMapping("create")
    public ApiResult create(@Valid @RequestBody Role role, BindingResult errors) {
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        srv.save(role);
        return new ApiResult().ok("entity.crud.create.success");
    }

    @PostMapping("update")
    public ApiResult update(@Valid @RequestBody Role role, BindingResult errors) {
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        srv.updateById(role);
        return new ApiResult().ok("entity.crud.update.success");
    }

    @PostMapping("refresh")
    public ApiResult refresh() {
        srv.refresh();
        return new ApiResult().ok("entity.crud.refresh.success");
    }

}
