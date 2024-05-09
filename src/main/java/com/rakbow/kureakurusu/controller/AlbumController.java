package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.AlbumTrackInfoQry;
import com.rakbow.kureakurusu.data.dto.AlbumUpdateTrackInfoDTO;
import com.rakbow.kureakurusu.service.item.AlbumService;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    //endregion

    //region advanced crud

    @PostMapping("update-track-info")
    public ApiResult updateTrackInfo(@RequestBody AlbumUpdateTrackInfoDTO cmd) {
        srv.updateAlbumTrackInfo(cmd.getId(), cmd.getDiscs());
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.update.success"));
    }

    @PostMapping("get-track-info")
    public ApiResult getTrackInfo(@RequestBody AlbumTrackInfoQry qry) {
        return new ApiResult().load(srv.getTrackInfo(qry.getId()));
    }

    //endregion

}
