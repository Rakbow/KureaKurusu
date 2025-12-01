package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.service.ItemService;
import com.rakbow.kureakurusu.service.ItemExtraService;
import com.rakbow.kureakurusu.toolkit.JsonUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Rakbow
 * @since 2024/4/11 4:06
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("db/item")
public class ItemController {

    private final ItemService srv;
    private final ItemExtraService extSrv;

    //region basic crud

    @PostMapping("create")
    public ApiResult advanceCreate(
            @RequestParam("param") String param,
            @RequestParam("images") MultipartFile[] images
    ) {
        ItemSuperCreateDTO dto = JsonUtil.to(param, ItemSuperCreateDTO.class);
        return ApiResult.ok(srv.create(dto, images));
    }

    @PostMapping("update")
    public ApiResult update(@Valid @RequestBody ItemUpdateDTO dto, BindingResult errors) {
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        srv.update(dto);
        return ApiResult.ok("entity.crud.update.success");
    }

    @DeleteMapping("delete")
    public ApiResult delete(@RequestBody ItemDeleteDTO dto) {
        srv.delete(dto.getIds());
        return ApiResult.ok("entity.crud.delete.success");
    }

    //endregion

    //region query

    @UniqueVisitor
    @PostMapping("detail/{id}")
    public ApiResult detail(@PathVariable("id") long id) {
        return ApiResult.ok(srv.detail(id));
    }

    @PostMapping("search")
    public ApiResult search(@RequestBody ItemSearchQueryDTO dto) {
        return ApiResult.ok(srv.search(dto));
    }

    @PostMapping("list")
    public ApiResult list(@RequestBody ItemListQueryDTO dto) {
        return ApiResult.ok(srv.list(dto));
    }

    //endregion

    //region extra api

    @PostMapping("album-track-list")
    public ApiResult getAlbumTracks(@RequestBody AlbumTrackInfoQry qry) {
        return ApiResult.ok(extSrv.getAlbumTracks(qry.getId()));
    }

    @PostMapping("album-track-quick-create")
    public ApiResult albumTrackQuickCreate(@RequestBody DiscCreateDTO dto) {
        extSrv.albumTrackQuickCreate(dto, true);
        return ApiResult.ok("entity.crud.create.success");
    }

    @PostMapping("album-track-quick-upload")
    public ApiResult albumTrackQuickUpload(
            @RequestParam("files") MultipartFile[] files,
            @ModelAttribute AlbumTrackQuickUploadDTO dto
    ) {
        extSrv.albumTrackQuickUpload(files, dto);
        return ApiResult.ok("entity.crud.upload.success");
    }

    @PostMapping("convert-isbn")
    public ApiResult convertISBN13(@RequestBody ISBNConvertDTO dto) {
        return ApiResult.ok(extSrv.convertISBN13(dto.isbn10()));
    }

    //endregion

}