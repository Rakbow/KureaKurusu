package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.annotation.Permission;
import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.common.R;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.service.ItemExtraService;
import com.rakbow.kureakurusu.service.ItemService;
import com.rakbow.kureakurusu.toolkit.JsonUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.rakbow.kureakurusu.data.constant.PermissionConstant.*;

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
    @Permission(ITEM_CREATE)
    public R advanceCreate(
            @RequestParam("param") String param,
            @RequestParam("images") MultipartFile[] images
    ) {
        ItemSuperCreateDTO dto = JsonUtil.to(param, ItemSuperCreateDTO.class);
        return R.ok(srv.create(dto, images));
    }

    @PostMapping("update")
    @Permission(ITEM_UPDATE)
    public R update(@Valid @RequestBody ItemUpdateDTO dto, BindingResult errors) {
        if (errors.hasErrors()) return new R().fail(errors);
        srv.update(dto);
        return R.ok("entity.crud.update.success");
    }

    @DeleteMapping("delete")
    @Permission(ITEM_DELETE)
    public R delete(@RequestBody CommonDeleteDTO dto) {
        srv.delete(dto.ids());
        return R.ok("entity.crud.delete.success");
    }

    //endregion

    //region query

    @UniqueVisitor
    @PostMapping("detail/{id}")
    public R detail(@PathVariable("id") long id) {
        return R.ok(srv.detail(id));
    }

    @PostMapping("search")
    public R search(@RequestBody ItemSearchQueryDTO dto) {
        return R.ok(srv.search(dto));
    }

    @PostMapping("list")
    @Permission(ITEM_QUERY_LIST)
    public R list(@RequestBody ItemListQueryDTO dto) {
        return R.ok(srv.list(dto));
    }

    //endregion

    //region extra api

    @PostMapping("album-track-list")
    public R getAlbumTracks(@RequestBody CommonDetailQry qry) {
        return R.ok(extSrv.getAlbumTracks(qry.id()));
    }

    @PostMapping("album-track-quick-create")
    @Permission(ADMIN)
    public R albumTrackQuickCreate(@RequestBody DiscCreateDTO dto) {
        extSrv.albumTrackQuickCreate(dto, true);
        return R.ok("entity.crud.create.success");
    }

    @PostMapping("album-track-quick-upload")
    @Permission(ADMIN)
    public R albumTrackQuickUpload(
            @RequestParam("files") MultipartFile[] files,
            @ModelAttribute AlbumTrackQuickUploadDTO dto
    ) {
        extSrv.albumTrackQuickUpload(files, dto);
        return R.ok("entity.crud.upload.success");
    }

    @PostMapping("convert-isbn")
    public R convertISBN13(@RequestBody ISBNConvertDTO dto) {
        return R.ok(extSrv.convertISBN13(dto.isbn10()));
    }

    //endregion

}