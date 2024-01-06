package com.rakbow.kureakurusu.controller.entity;

import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.controller.interceptor.AuthorityInterceptor;
import com.rakbow.kureakurusu.data.ApiResult;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.dto.album.*;
import com.rakbow.kureakurusu.data.dto.base.ListQry;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.entity.Album;
import com.rakbow.kureakurusu.data.entity.Music;
import com.rakbow.kureakurusu.data.vo.album.AlbumDetailVO;
import com.rakbow.kureakurusu.service.AlbumService;
import com.rakbow.kureakurusu.service.GeneralService;
import com.rakbow.kureakurusu.service.MusicService;
import com.rakbow.kureakurusu.service.PersonService;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.EntityUtil;
import com.rakbow.kureakurusu.util.convertMapper.entity.AlbumVOMapper;
import com.rakbow.kureakurusu.util.entity.MusicUtil;
import com.rakbow.kureakurusu.util.file.CommonImageUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    private final AlbumService service;
    private final MusicService musicService;
    private final EntityUtil entityUtil;
    private final GeneralService generalService;
    private final PersonService personService;
    private final AlbumVOMapper VOMapper = AlbumVOMapper.INSTANCES;
    //endregion

    // region ------basic crud------
    @PostMapping("detail")
    @UniqueVisitor
    public ApiResult getAlbumDetailData(@RequestBody AlbumDetailQry qry) {
        ApiResult res = new ApiResult();
        try {
            Album album = service.getAlbum(qry.getId());

            if (album == null)
                return res.fail(I18nHelper.getMessage("entity.url.error", Entity.ALBUM.getName()));

            List<Music> musics = musicService.getMusicsByAlbumId(qry.getId());

            String coverUrl = CommonImageUtil.getCoverUrl(album.getImages());


            res.data = AlbumDetailVO.builder()
                    .album(service.buildVO(album, musics))
                    .audioInfos(AuthorityInterceptor.isUser()
                            ? MusicUtil.getMusicAudioInfo(musicService.getMusicsByAlbumId(qry.getId()), coverUrl)
                            : null)
                    .options(entityUtil.getDetailOptions(Entity.ALBUM.getValue()))
                    .detailInfo(entityUtil.getItemDetailInfo(album, Entity.ALBUM.getValue()))
                    .pageInfo(generalService.getPageTraffic(Entity.ALBUM.getValue(), qry.getId()))
                    .itemImageInfo(CommonImageUtil.segmentImages(album.getImages(), 185, Entity.ALBUM, false))
                    .personnel(personService.getPersonnel(Entity.ALBUM.getValue(), qry.getId()))
                    .build();
        }catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage());
        }
        return res;
    }

    //根据搜索条件获取专辑
    @SuppressWarnings("unchecked")
    @PostMapping("list")
    public ApiResult getAlbums(@RequestBody ListQry qry) {
        ApiResult res = new ApiResult();
        try{
            SearchResult<Album> result = service.getAlbums(new QueryParams(qry));
             res.data = VOMapper.toVOAlpha(result.data);
             res.total = result.total;
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
            service.save(album);
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
            service.updateAlbum(album);
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
            //delete album
            service.deleteAlbums(cmd.getIds());
            //delete music
            musicService.deleteMusicsByAlbumIds(cmd.getIds());
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
    @PostMapping("update-trackInfo")
    public ApiResult updateAlbumTrackInfo(@RequestBody UpdateAlbumTrackInfoCmd cmd) {
        ApiResult res = new ApiResult();
        try {
            service.updateAlbumTrackInfo(cmd.getId(), cmd.getDiscList());
            res.ok(I18nHelper.getMessage("entity.curd.update.success", Entity.ALBUM.getName()));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage());
        }
        return res;
    }

    // @PostMapping("get-related-albums")
    // public String getRelatedAlbums(@RequestBody AlbumDetailQry qry) {
    //     ApiResult res = new ApiResult();
    //     try {
    //         long id = qry.getId();
    //         // res.data = albumService.getRelatedAlbums(id);
    //
    //     }catch (Exception e) {
    //         res.setErrorMessage(e);
    //     }
    //     return res.toJson();
    // }

    //endregion

}
