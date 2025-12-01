package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.FavListItemListQueryDTO;
import com.rakbow.kureakurusu.data.dto.FavListQueryDTO;
import com.rakbow.kureakurusu.data.dto.ListItemCreateDTO;
import com.rakbow.kureakurusu.data.entity.FavList;
import com.rakbow.kureakurusu.service.ListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * @author Rakbow
 * @since 2025/7/25 4:00
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("db/list")
public class ListController {

    private final ListService srv;

    @PostMapping("detail/{id}")
    public ApiResult detail(@PathVariable("id") long id) {
        return ApiResult.ok(srv.detail(id));
    }

    @PostMapping("create")
    public ApiResult create(@Valid @RequestBody FavList dto, BindingResult errors) {
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        srv.create(dto);
        return ApiResult.ok("entity.crud.create.success");
    }

    @PostMapping("lists")
    public ApiResult lists(@RequestBody FavListQueryDTO dto) {
        return ApiResult.ok(srv.lists(dto));
    }

    @PostMapping("add-items")
    public ApiResult addItems(@RequestBody ListItemCreateDTO dto) {
        srv.addItems(dto);
        return ApiResult.ok("entity.crud.create.success");
    }

    @PostMapping("get-items")
    public ApiResult getItems(@RequestBody FavListItemListQueryDTO dto) {
        return ApiResult.ok(srv.getItems(dto));
    }

}
