package com.rakbow.kureakurusu.controller.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.controller.interceptor.AuthorityInterceptor;
import com.rakbow.kureakurusu.data.ApiResult;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.dto.album.AlbumAddDTO;
import com.rakbow.kureakurusu.data.dto.album.AlbumDeleteCmd;
import com.rakbow.kureakurusu.data.dto.album.AlbumDetailQry;
import com.rakbow.kureakurusu.data.dto.album.AlbumUpdateDTO;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.vo.album.AlbumVOAlpha;
import com.rakbow.kureakurusu.entity.Album;
import com.rakbow.kureakurusu.entity.Music;
import com.rakbow.kureakurusu.service.*;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.common.EntityUtil;
import com.rakbow.kureakurusu.util.convertMapper.entity.AlbumVOMapper;
import com.rakbow.kureakurusu.util.entity.MusicUtil;
import com.rakbow.kureakurusu.util.file.CommonImageUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2022-08-07 21:53
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/db/album")
public class AlbumController {

    //region ------引入实例------

    private static final Logger logger = LoggerFactory.getLogger(AlbumController.class);

    private final AlbumService service;
    private final MusicService musicService;
    private final EntityUtil entityUtil;
    private final GeneralService generalService;
    private final PersonService personService;
    private final AlbumVOMapper VOMapper = AlbumVOMapper.INSTANCES;
    //endregion//region ------基础增删改------

    @PostMapping("detail")
    @UniqueVisitor
    public ApiResult getAlbumDetailData(@RequestBody AlbumDetailQry qry) {
        ApiResult res = new ApiResult();
        try {
            Album album = service.getAlbumWithAuth(qry.getId());

            if (album == null)
                return res.fail(I18nHelper.getMessage("entity.url.error", Entity.ALBUM.getNameZh()));

            List<Music> musics = musicService.getMusicsByAlbumId(qry.getId());

            String coverUrl = CommonImageUtil.getCoverUrl(album.getImages());

            JSONObject detailResult = new JSONObject();

            detailResult.put("album", service.buildVO(album, musics));

            if(AuthorityInterceptor.isUser()) {
                detailResult.put("audioInfos", MusicUtil.getMusicAudioInfo(musicService.getMusicsByAlbumId(qry.getId()), coverUrl));
            }

            detailResult.put("options", entityUtil.getDetailOptions(Entity.ALBUM.getId()));
            detailResult.put("detailInfo", entityUtil.getItemDetailInfo(album, Entity.ALBUM.getId()));
            detailResult.put("pageInfo", generalService.getPageTraffic(Entity.ALBUM.getId(), qry.getId()));
            detailResult.put("itemImageInfo", CommonImageUtil.segmentImages(album.getImages(), 185, Entity.ALBUM, false));
            detailResult.put("personnel", personService.getPersonnel(Entity.ALBUM.getId(), qry.getId()));

            res.data = detailResult;
        }catch (Exception e) {
            res.fail(e);
        }
        return res;
    }

    //根据搜索条件获取专辑
    @SuppressWarnings("unchecked")
    @PostMapping("list")
    public ApiResult getAlbums(@RequestBody JSONObject param) {
        ApiResult res = new ApiResult();
        try{
            String pageLabel = param.getString("pageLabel");

            List<AlbumVOAlpha> albums = new ArrayList<>();

            SearchResult searchResult = service.getAlbums(new QueryParams(param));

            if (StringUtils.equals(pageLabel, "list")) {
                albums = VOMapper.toVOAlpha((List<Album>) searchResult.data);
            }
            if (StringUtils.equals(pageLabel, "index")) {
                albums = VOMapper.toVOAlpha((List<Album>) searchResult.data);
            }

            JSONObject result = new JSONObject();
            result.put("data", albums);
            result.put("total", searchResult.total);
            res.data = result;
        }catch (Exception e) {
        res.fail(e);
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
            service.addAlbum(album);
            res.ok(I18nHelper.getMessage("entity.curd.insert.success", Entity.ALBUM.getNameZh()));
        } catch (Exception e) {
            res.fail(e);
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
            res.ok(I18nHelper.getMessage("entity.curd.delete.success", Entity.ALBUM.getNameZh()));
        } catch (Exception e) {
            res.fail(e);
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
            res.ok(I18nHelper.getMessage("entity.curd.update.success", Entity.ALBUM.getNameZh()));
        } catch (Exception e) {
            res.fail(e);
        }
        return res;
    }

    //endregion

    //region ------进阶增删改------

    //更新专辑音轨信息TrackInfo
    @RequestMapping(path = "/update-trackInfo", method = RequestMethod.POST)
    @ResponseBody
    public String updateAlbumTrackInfo(@RequestBody JSONObject json) {
        ApiResult res = new ApiResult();
        try {
            int id = json.getIntValue("id");
            String discList = json.getString("discList");

            service.updateAlbumTrackInfo(id, discList);

            res.message = I18nHelper.getMessage("entity.curd.update.success", Entity.ALBUM.getNameZh());
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

    @RequestMapping(value = "/get-related-albums", method = RequestMethod.POST)
    @ResponseBody
    public String getRelatedAlbums(@RequestBody JSONObject json) {
        ApiResult res = new ApiResult();
        try {
            int id = json.getIntValue("id");
            // res.data = albumService.getRelatedAlbums(id);

        }catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

    //endregion

}
