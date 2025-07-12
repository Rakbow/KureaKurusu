package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.service.ItemService;
import com.rakbow.kureakurusu.service.item.AlbumService;
import com.rakbow.kureakurusu.service.item.BookService;
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
    private final AlbumService albumSrv;
    private final BookService bookSrv;

    //region basic crud

    @PostMapping("create")
    public ApiResult advanceCreate(
            @RequestParam("param") String param,
            @RequestParam("images") MultipartFile[] images
    ) {
        ItemSuperCreateDTO dto = JsonUtil.to(param, ItemSuperCreateDTO.class);
        //check
        // if (errors.hasErrors()) return new ApiResult().fail(errors);
        //save
        return new ApiResult().load(srv.create(dto, images));
    }

    @PostMapping("update")
    public ApiResult update(@Valid @RequestBody ItemUpdateDTO dto, BindingResult errors) {
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        srv.update(dto);
        return new ApiResult().ok("entity.crud.update.success");
    }

    @DeleteMapping("delete")
    public ApiResult delete(@RequestBody ItemDeleteDTO dto) {
        srv.delete(dto.getIds());
        return new ApiResult().ok("entity.crud.delete.success");
    }

    //endregion

    //region query

    @UniqueVisitor
    @PostMapping("detail/{id}")
    public ApiResult detail(@PathVariable("id") long id) {
        return new ApiResult().load(srv.detail(id));
    }

    @PostMapping("search")
    public ApiResult search(@RequestBody ItemSearchQueryDTO dto) {
        return new ApiResult().load(srv.search(dto));
    }

    @PostMapping("list")
    public ApiResult list(@RequestBody ItemListQueryDTO dto) {
        return new ApiResult().load(srv.list(dto));
    }

    //endregion

    //region extra api

    @PostMapping("album-track-list")
    public ApiResult getAlbumTracks(@RequestBody AlbumTrackInfoQry qry) {
        return new ApiResult().load(albumSrv.getAlbumTracks(qry.getId()));
    }

    @PostMapping("album-track-quick-create")
    public ApiResult quickCreateAlbumTrack(@RequestBody AlbumDiscCreateDTO dto) {
        albumSrv.quickCreateAlbumTrack(dto, true);
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
    public ApiResult convertISBN(@RequestBody ISBNConvertDTO dto) {
        return new ApiResult().load(bookSrv.convertISBN(dto.getIsbn10()));
    }

    //endregion

}