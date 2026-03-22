package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.annotation.Permission;
import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.IndexItemListQueryDTO;
import com.rakbow.kureakurusu.data.dto.IndexListQueryDTO;
import com.rakbow.kureakurusu.data.dto.ListItemCreateDTO;
import com.rakbow.kureakurusu.data.entity.Index;
import com.rakbow.kureakurusu.service.IndexService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.rakbow.kureakurusu.data.constant.PermissionConstant.*;

/**
 * @author Rakbow
 * @since 2025/7/25 4:00
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("db/index")
public class IndexController {

    private final IndexService srv;

    @PostMapping("detail/{id}")
    public ApiResult detail(@PathVariable("id") long id) {
        return ApiResult.ok(srv.detail(id));
    }

    @PostMapping("create")
    @Permission(INDEX_CREATE)
    public ApiResult create(@Valid @RequestBody Index dto, BindingResult errors) {
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        srv.create(dto);
        return ApiResult.ok("entity.crud.create.success");
    }

    @PostMapping("list")
    public ApiResult lists(@RequestBody IndexListQueryDTO dto) {
        return ApiResult.ok(srv.list(dto));
    }

    @PostMapping("add-items")
    @Permission(INDEX_ADD_ITEM)
    public ApiResult addItems(@RequestBody ListItemCreateDTO dto) {
        srv.addItems(dto);
        return ApiResult.ok("entity.crud.create.success");
    }

    @PostMapping("get-items")
    public ApiResult getItems(@RequestBody IndexItemListQueryDTO dto) {
        return ApiResult.ok(srv.getItems(dto));
    }

}
