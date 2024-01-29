package com.rakbow.kureakurusu.controller.entity;

import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.dto.album.*;
import com.rakbow.kureakurusu.data.dto.base.ListQry;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.entity.Album;
import com.rakbow.kureakurusu.data.system.ApiResult;
import com.rakbow.kureakurusu.data.vo.album.AlbumDetailVO;
import com.rakbow.kureakurusu.service.AlbumService;
import com.rakbow.kureakurusu.service.PersonService;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.convertMapper.entity.AlbumVOMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * @author Rakbow
 * @since 2022-08-07 21:53
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/db/album")
public class AlbumController {

    //region ------inject------
    private static final Logger log = LoggerFactory.getLogger(AlbumController.class);
    private final AlbumService srv;
    private final PersonService personSrv;
    private final AlbumVOMapper VOMapper;

    private final int ENTITY_VALUE = Entity.ALBUM.getValue();
    //endregion

    // region ------basic crud------
    @PostMapping("detail")
    @UniqueVisitor
    public ApiResult getAlbumDetailData(@RequestBody AlbumDetailQry qry) {
        ApiResult res = new ApiResult();
        try {
            AlbumDetailVO vo = srv.getDetail(qry);
            vo.setPersonnel(personSrv.getPersonnel(ENTITY_VALUE, qry.getId()));
            res.loadData(vo);
        }catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage());
        }
        return res;
    }

    //根据搜索条件获取专辑
    @PostMapping("list")
    public ApiResult getAlbums(@RequestBody ListQry qry) {
        ApiResult res = new ApiResult();
        try{
            res.loadData(srv.getAlbums(new QueryParams(qry)));
        }catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage());
        }
        return res;
    }

    //新增专辑
    @PostMapping("add")
    public ApiResult addAlbum(@Valid @RequestBody AlbumAddDTO dto, BindingResult errors) {
        ApiResult res = new ApiResult();
        try {
            //check
            if (errors.hasErrors())
                return res.fail(errors);
            //build
            Album album = VOMapper.build(dto);
            //save
            srv.save(album);
            res.ok(I18nHelper.getMessage("entity.curd.insert.success", Entity.ALBUM.getName()));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage());
        }
        return res;
    }

    //更新专辑基础信息
    @PostMapping("update")
    public ApiResult updateAlbum(@Valid @RequestBody AlbumUpdateDTO dto, BindingResult errors) {
        ApiResult res = new ApiResult();
        try {
            //check
            if (errors.hasErrors())
                return res.fail(errors);
            //build
            Album album = VOMapper.build(dto);
            //save
            srv.updateAlbum(album);
            res.ok(I18nHelper.getMessage("entity.curd.update.success", Entity.ALBUM.getName()));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage());
        }
        return res;
    }

    //删除专辑(单个/多个)
    @DeleteMapping("delete")
    public ApiResult deleteAlbum(@RequestBody AlbumDeleteCmd cmd) {
        ApiResult res = new ApiResult();
        try {
            srv.deleteAlbums(cmd.getIds());
            res.ok(I18nHelper.getMessage("entity.curd.delete.success", Entity.ALBUM.getName()));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage());
        }
        return res;
    }

    //endregion

    //region ------advanced crud------

    //更新专辑音轨信息TrackInfo
    @PostMapping("update-track-info")
    public ApiResult updateAlbumTrackInfo(@RequestBody UpdateAlbumTrackInfoCmd cmd) {
        ApiResult res = new ApiResult();
        try {
            srv.updateAlbumTrackInfo(cmd.getId(), cmd.getDiscs());
            res.ok(I18nHelper.getMessage("entity.curd.update.success", Entity.ALBUM.getName()));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage());
        }
        return res;
    }

    @PostMapping("get-track-info")
    public ApiResult getAlbumTrackInfo(@RequestBody AlbumTrackInfoQry qry) {
        ApiResult res = new ApiResult();
        try {
            res.loadData(srv.getTrackInfo(srv.getById(qry.getId())));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage());
        }
        return res;
    }

    //endregion

}
