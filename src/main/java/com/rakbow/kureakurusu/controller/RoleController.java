package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.annotation.Permission;
import com.rakbow.kureakurusu.data.common.R;
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

import static com.rakbow.kureakurusu.data.constant.PermissionConstant.*;

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
    @Permission(ROLE_QUERY_LIST)
    public R list(@RequestBody RoleListQueryDTO dto) {
        return R.ok(srv.list(dto));
    }

    @PostMapping("create")
    @Permission(ROLE_CREATE)
    public R create(@Valid @RequestBody Role role, BindingResult errors) {
        if (errors.hasErrors()) return new R().fail(errors);
        srv.save(role);
        return R.ok("entity.crud.create.success");
    }

    @PostMapping("update")
    @Permission(ROLE_UPDATE)
    public R update(@Valid @RequestBody Role role, BindingResult errors) {
        if (errors.hasErrors()) return new R().fail(errors);
        srv.updateById(role);
        return R.ok("entity.crud.update.success");
    }

    @PostMapping("refresh")
    @Permission(ROLE_REFRESH)
    public R refresh() {
        srv.refresh();
        return R.ok("entity.crud.refresh.success");
    }

}
