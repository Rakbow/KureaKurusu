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
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("update")
    @Permission(INDEX_UPDATE)
    public R update(@Valid @RequestBody IndexDTO.IndexUpdateDTO dto, BindingResult errors) {
        if (errors.hasErrors()) return new R().fail(errors);
        srv.update(dto);
        return R.ok("entity.crud.update.success");
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

    @PostMapping("add-element")
    @Permission(INDEX_ELEMENT_ADD)
    public R addElement(@RequestBody IndexItemCreateDTO dto) {
        srv.addElement(dto);
        return R.ok("entity.crud.create.success");
    }

    @PostMapping("update-element")
    @Permission(INDEX_ELEMENT_UPDATE)
    public R updateElement(@RequestBody IndexDTO.IndexElementUpdateDTO dto) {
        srv.updateElement(dto);
        return R.ok("entity.crud.update.success");
    }

    @PostMapping("upload-image")
    @Permission(INDEX_UPDATE_COVER)
    public R uploadImage(@RequestParam("id") int id, @RequestParam("file") MultipartFile file) {
        return new R().ok(srv.uploadImage(id, file), "entity.crud.update.success");
    }

}
