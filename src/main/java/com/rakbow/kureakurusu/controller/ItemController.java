package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.service.ItemService;
import com.rakbow.kureakurusu.service.RelationService;
import com.rakbow.kureakurusu.service.item.AlbumService;
import com.rakbow.kureakurusu.service.item.BookService;
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
    private final AlbumService albumSrv;
    private final BookService bookSrv;
    private final RelationService reSrv;

    //region basic crud

    @PostMapping("create")
    public ApiResult create(@Valid @RequestBody ItemCreateDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //save
        srv.create(dto);
        return new ApiResult().ok("entity.crud.create.success");
    }

    @PostMapping("update")
    public ApiResult update(@Valid @RequestBody ItemUpdateDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //update
        srv.update(dto);
        return new ApiResult().ok("entity.crud.update.success");
    }

    @DeleteMapping("delete")
    public ApiResult delete(@RequestBody ItemDeleteDTO dto) {
        srv.delete(dto.getIds());
        return new ApiResult().ok("entity.curd.delete.success");
    }

    //endregion

    //region query

    @UniqueVisitor
    @PostMapping("detail/{id}")
    public ApiResult detail(@PathVariable("id") long id) {
        return new ApiResult().load(srv.detail(id));
    }

    @PostMapping("search")
    public ApiResult search(@RequestBody ItemSearchParams param) {
        return new ApiResult().load(srv.search(param));
    }

    @PostMapping("list")
    public ApiResult list(@RequestBody ListQuery dto) {
        return new ApiResult().load(srv.list(dto));
    }

    //endregion

    //region advance crud

    @PostMapping("create-advance")
    public ApiResult advanceCreate(@Valid @RequestBody ItemSuperCreateDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //save
        return new ApiResult().load(srv.advanceCreate(dto.getItem(), dto.getImages(), dto.getRelatedEntities(), dto.getGenerateThumb()));
    }

    //endregion

    //region extra api

    @PostMapping("album-track-list")
    public ApiResult getAlbumTracks(@RequestBody AlbumTrackInfoQry qry) {
        return new ApiResult().load(albumSrv.getAlbumTracks(qry.getId()));
    }

    @PostMapping("album-track-quick-create")
    public ApiResult quickCreateAlbumTrack(@RequestBody AlbumTrackQuickCreateDTO dto) {
        albumSrv.quickCreateAlbumTrack(dto.getId(), dto.getDisc(), true);
        return new ApiResult().ok("entity.crud.create.success");
    }

    @PostMapping("album-track-files-upload")
    public ApiResult uploadAlbumTrackFiles(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("albumId") long albumId
    ) {
        albumSrv.uploadAlbumTrackFiles(files, albumId);
        return new ApiResult().ok("entity.crud.create.success");
    }

    @PostMapping("convert-isbn")
    public ApiResult convertISBN(@RequestBody String isbn10) {
        return new ApiResult().load(bookSrv.convertISBN(isbn10));
    }

    @PostMapping("get-extra-info")
    public ApiResult getExtraInfo(@RequestBody CommonDetailQry qry) {
        return new ApiResult().load(reSrv.getItemExtraInfo(qry.getId()));
    }

    //endregion

}