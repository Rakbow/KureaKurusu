package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.annotation.Permission;
import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.common.R;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.service.EntryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.rakbow.kureakurusu.data.constant.PermissionConstant.*;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/7/2 21:59
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("db/entry")
public class EntryController {

    private final EntryService srv;

    @UniqueVisitor
    @PostMapping("detail/{id}")
    public R detail(@PathVariable("id") long id) {
        return R.ok(srv.detail(id));
    }

    @PostMapping("create")
    @Permission(ENTRY_CREATE)
    public R create(@Valid @RequestBody EntryDTO.EntrySuperCreateDTO dto, BindingResult errors) {
        if (errors.hasErrors()) return new R().fail(errors);
        return R.ok(srv.create(dto));
    }

    @PostMapping("update")
    @Permission(ENTRY_UPDATE)
    public R update(@Valid @RequestBody EntryDTO.EntryUpdateDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new R().fail(errors);
        //update
        srv.update(dto);
        return R.ok("entity.crud.update.success");
    }

    @PostMapping("list")
    @Permission(ENTRY_QUERY_LIST)
    public R list(@RequestBody EntryDTO.EntryListQueryDTO dto) {
        return R.ok(srv.list(dto));
    }

    @PostMapping("search")
    public R search(@RequestBody EntryDTO.EntrySearchQueryDTO param) {
        return R.ok(srv.search(param));
    }

    @PostMapping("upload-image")
    @Permission(ENTRY_UPLOAD_IMAGE)
    public R uploadImage(
            @RequestParam("id") int id,
            @RequestParam("file") MultipartFile file,
            @RequestParam("imageType") int imageType
    ) {
        return new R().ok(
                srv.uploadImage(id, imageType, file),
                "entity.crud.update.success"
        );
    }

    @PostMapping("mini")
    public R mini(@RequestBody List<Long> ids) {
        return R.ok(srv.getMiniVO(ids));
    }

    @PostMapping("get-sub-products")
    public R getSubProducts(@RequestBody CommonDetailQry qry) {
        return R.ok(srv.getSubProducts(qry.id()));
    }

    @PostMapping("get-extra-info")
    public R getExtraInfo(@RequestBody CommonDetailQry qry) {
        return R.ok(srv.getItemExtraInfo(qry.id()));
    }

}
