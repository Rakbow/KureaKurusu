package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.dto.AlbumQuickCreateDiscDTO;
import com.rakbow.kureakurusu.data.dto.AlbumTrackInfoQry;
import com.rakbow.kureakurusu.data.dto.AlbumUpdateTrackInfoDTO;
import com.rakbow.kureakurusu.data.dto.BookIsbnDTO;
import com.rakbow.kureakurusu.service.RelationService;
import com.rakbow.kureakurusu.service.item.AlbumService;
import com.rakbow.kureakurusu.service.item.BookService;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rakbow
 * @since 2024/5/14 16:48
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("db/item")
public class ItemExtendController {

    //region inject
    private final AlbumService albumSrv;
    private final BookService bookSrv;
    private final RelationService reSrv;
    //endregion

    @PostMapping("quick-add-album-disc")
    public ApiResult batchAddAlbumTrack(@RequestBody AlbumQuickCreateDiscDTO dto) {
        albumSrv.quickCreateAlbumDisc(dto.getId(), dto.getSerial(), dto.getTracks());
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.update.success"));
    }

    @PostMapping("update-album-track-info")
    public ApiResult updateTrackInfo(@RequestBody AlbumUpdateTrackInfoDTO cmd) {
        albumSrv.updateAlbumTrackInfo(cmd.getId(), cmd.getDiscs());
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.update.success"));
    }

    @PostMapping("get-album-track-info")
    public ApiResult getTrackInfo(@RequestBody AlbumTrackInfoQry qry) {
        return new ApiResult().load(albumSrv.getTrackInfo(qry.getId()));
    }


    @PostMapping("get-isbn")
    public ApiResult getISBN(@RequestBody BookIsbnDTO dto) {
        return new ApiResult().load(bookSrv.getISBN(dto.getLabel(), dto.getIsbn()));
    }

    @PostMapping("get-exc-related-entries")
    public ApiResult getRelatedEntries(@RequestBody CommonDetailQry qry) {
        return new ApiResult().load(reSrv.getItemRelatedSubject(qry.getId()));
    }

}
