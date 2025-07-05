package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.CommonDetailQry;
import com.rakbow.kureakurusu.data.dto.EntrySearchParams;
import com.rakbow.kureakurusu.data.dto.EntryUpdateDTO;
import com.rakbow.kureakurusu.data.dto.ListQuery;
import com.rakbow.kureakurusu.service.EntryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ApiResult detail(@PathVariable("id") long id) {
        return new ApiResult().load(srv.detail(id));
    }

    @PostMapping("update")
    public ApiResult update(@Valid @RequestBody EntryUpdateDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //update
        srv.update(dto);
        return new ApiResult().ok("entity.crud.update.success");
    }

    @PostMapping("list")
    public ApiResult list(@RequestBody ListQuery dto) {
        return new ApiResult().load(srv.list(dto));
    }

    @PostMapping("search")
    public ApiResult search(@RequestBody EntrySearchParams param) {
        return new ApiResult().load(srv.search(param));
    }

    @PostMapping("upload-image")
    public ApiResult uploadImage(
            @RequestParam("id") int id,
            @RequestParam("file") MultipartFile file,
            @RequestParam("imageType") int imageType
    ) {
        return new ApiResult().ok(
                srv.uploadImage(id, imageType, file),
                "entity.crud.update.success"
        );
    }

    @PostMapping("mini")
    public ApiResult mini(@RequestBody List<Long> ids) {
        return new ApiResult().load(srv.getMiniVO(ids));
    }

    @PostMapping("get-sub-products")
    public ApiResult getSubProducts(@RequestBody CommonDetailQry qry) {
        return new ApiResult().load(srv.getSubProducts(qry.getId()));
    }

    @PostMapping("get-extra-info")
    public ApiResult getExtraInfo(@RequestBody CommonDetailQry qry) {
        return new ApiResult().load(srv.getItemExtraInfo(qry.getId()));
    }

}
