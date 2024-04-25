package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.emun.Entity;
import com.rakbow.kureakurusu.data.entity.Album;
import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.vo.album.AlbumDetailVO;
import com.rakbow.kureakurusu.service.AlbumService;
import com.rakbow.kureakurusu.service.ItemService;
import com.rakbow.kureakurusu.service.PersonService;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.convertMapper.AlbumVOMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * @author Rakbow
 * @since 2022-08-07 21:53
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("db/album")
public class AlbumController {

    //region inject
    private final AlbumService srv;
    private final ItemService itemSrv;
    private final PersonService personSrv;
    private final AlbumVOMapper VOMapper;

    private final int ENTITY_VALUE = Entity.ALBUM.getValue();
    //endregion

    // region basic crud
    @PostMapping("detail")
    @UniqueVisitor
    public ApiResult detail(@RequestBody AlbumDetailQry qry) {
        AlbumDetailVO vo = srv.detail(qry);
        vo.setPersonnel(personSrv.getPersonnel(ENTITY_VALUE, qry.getId()));
        return new ApiResult().load(vo);
    }

    @PostMapping("list")
    public ApiResult list(@RequestBody ListQry qry) {
        return new ApiResult().load(srv.getAlbums(new AlbumListParams(qry)));
    }

//    @PostMapping("list")
//    public ApiResult list(@RequestBody ListQry qry) {
//        return new ApiResult().load(itemSrv.joinPageTest(new AlbumListParams(qry)));
//    }

    @PostMapping("search")
    public ApiResult search(@RequestBody SearchQry qry) {
        return new ApiResult().load(srv.searchAlbums(qry));
    }

    @PostMapping("add")
    public ApiResult add(@Valid @RequestBody AlbumAddDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //build
        Album album = VOMapper.build(dto);
        //save
        srv.save(album);
        return  new ApiResult().ok(I18nHelper.getMessage("entity.curd.insert.success", Entity.ALBUM.getName()));
    }

    @PostMapping("update")
    public ApiResult update(@Valid @RequestBody AlbumItemUpdateDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //save
        srv.updateById(new Album(dto));
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.update.success", Entity.ALBUM.getName()));
    }

    @DeleteMapping("delete")
    public ApiResult delete(@RequestBody DeleteCmd cmd) {
        srv.deleteAlbums(cmd.getIds());
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.delete.success", Entity.ALBUM.getName()));
    }

    //endregion

    //region advanced crud

    @PostMapping("update-track-info")
    public ApiResult updateTrackInfo(@RequestBody AlbumUpdateTrackInfoCmd cmd) {
        srv.updateAlbumTrackInfo(cmd.getId(), cmd.getDiscs());
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.update.success", Entity.ALBUM.getName()));
    }

    @PostMapping("get-track-info")
    public ApiResult getTrackInfo(@RequestBody AlbumTrackInfoQry qry) {
        return new ApiResult().load(srv.getTrackInfo(srv.getById(qry.getId())));
    }

    //endregion

}
