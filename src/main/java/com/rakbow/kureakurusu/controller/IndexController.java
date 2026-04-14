package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.annotation.Permission;
import com.rakbow.kureakurusu.data.common.R;
import com.rakbow.kureakurusu.data.dto.*;
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
    public R detail(@PathVariable("id") long id) {
        return R.ok(srv.detail(id));
    }

    @PostMapping("create")
    @Permission(INDEX_CREATE)
    public R create(@Valid @RequestBody Index dto, BindingResult errors) {
        if (errors.hasErrors()) return new R().fail(errors);
        srv.create(dto);
        return R.ok("entity.crud.create.success");
    }

    @PostMapping("list")
    public R list(@RequestBody IndexListQueryDTO dto) {
        return R.ok(srv.list(dto));
    }

    @PostMapping("search")
    public R search(@RequestBody IndexListQueryDTO dto) {
        return R.ok(srv.search(dto));
    }

    @PostMapping("get-items")
    public R getItems(@RequestBody IndexItemSearchQueryDTO dto) {
        return R.ok(srv.getItems(dto));
    }

    @PostMapping("add-items")
    @Permission(INDEX_ADD_ITEM)
    public R addItems(@RequestBody ListItemCreateDTO dto) {
        srv.addItems(dto);
        return R.ok("entity.crud.create.success");
    }

    // @PostMapping("get-items")
    // public ApiResult getItems(@RequestBody IndexItemListQueryDTO dto) {
    //     return ApiResult.ok(srv.getItems(dto));
    // }

}
