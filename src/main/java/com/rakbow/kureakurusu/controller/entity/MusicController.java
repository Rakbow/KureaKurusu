package com.rakbow.kureakurusu.controller.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.ApiResult;
import com.rakbow.kureakurusu.data.dto.music.MusicDeleteFileCmd;
import com.rakbow.kureakurusu.data.system.File;
import com.rakbow.kureakurusu.data.entity.Music;
import com.rakbow.kureakurusu.service.*;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.common.EntityUtil;
import com.rakbow.kureakurusu.util.convertMapper.entity.MusicVOMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Rakbow
 * @since 2022-11-06 19:50
 */
@Controller
@RequestMapping("/db/music")
public class MusicController {

    private static final Logger logger = LoggerFactory.getLogger(MusicController.class);

    //region ------引入实例------
    @Resource
    private MusicService musicService;
    @Resource
    private AlbumService albumService;
    @Resource
    private UserService userService;
    @Resource
    private EntityUtil entityUtil;
    @Resource
    private EntityService entityService;
    

    private final MusicVOMapper musicVOMapper = MusicVOMapper.INSTANCES;
    //endregion

    //获取单个音频详细信息页面
    // @UniqueVisitor
    // @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    // public String getMusicDetail(@PathVariable("id") int id, Model model) {
    //     Music music = musicService.getMusicWithAuth(id);
    //     if (music == null) {
    //         model.addAttribute("errorMessage", String.format(ApiInfo.GET_DATA_FAILED_404, Entity.MUSIC.getNameZh()));
    //         return "/error/404";
    //     }
    //     Album album = albumService.getAlbum(music.getAlbumId());
    //     String coverUrl = CommonImageUtil.getCoverUrl(album.getImages());
    //
    //     model.addAttribute("music", musicVOMapper.music2VO(music, coverUrl));
    //
    //     if(AuthorityInterceptor.isUser()) {
    //         model.addAttribute("audioInfo", MusicUtil.getMusicAudioInfo(music, coverUrl));
    //     }
    //     //前端选项数据
    //     model.addAttribute("options", entityUtil.getDetailOptions(Entity.MUSIC.getId()));
    //     //获取页面数据
    //     model.addAttribute("pageInfo", entityService.getPageInfo(Entity.MUSIC.getId(), id, music));
    //     //实体类通用信息
    //     model.addAttribute("detailInfo", EntityUtil.getMetaDetailInfo(music, Entity.MUSIC.getId()));
    //     //获取同属一张碟片的音频
    //     model.addAttribute("relatedMusics", musicService.getRelatedMusics(music, coverUrl));
    //     //获取所属专辑的信息
    //     model.addAttribute("relatedAlbum", AlbumVOMapper.INSTANCES.toVOBeta(album));
    //
    //     return "/database/itemDetail/music-detail";
    //
    // }

    //更新Music
    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String updateMusic(@RequestBody  String json) {
        ApiResult res = new ApiResult();
        try{
            JSONObject param = JSON.parseObject(json);
            Music music = entityService.json2Entity(param, Music.class);

            //检测数据
            String errorMsg= musicService.checkMusicJson(param);
            if(!StringUtils.isBlank(errorMsg)) {
                res.setErrorMessage(errorMsg);
                return res.toJson();
            }

            //修改编辑时间
            music.setEditedTime(DateHelper.now());

            res.message = musicService.updateMusic(music.getId(), music);
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return res.toJson();
    }

    //更新music创作人员信息
    @RequestMapping(path = "/update-artists", method = RequestMethod.POST)
    @ResponseBody
    public String updateMusicArtists(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            int id = JSON.parseObject(json).getInteger("id");
            String artists = JSON.parseObject(json).get("artists").toString();

            res.message = musicService.updateMusicArtists(id, artists);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

    //更新歌词文本
    @RequestMapping(path = "/update-lyrics-text", method = RequestMethod.POST)
    @ResponseBody
    public String updateMusicLyricsText(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            int id = JSON.parseObject(json).getInteger("id");
            String lyricsText = JSON.parseObject(json).get("lyricsText").toString();

            res.message = musicService.updateMusicLyricsText(id, lyricsText);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

    //新增音频文件
    @RequestMapping(path = "/upload-file", method = RequestMethod.POST)
    @ResponseBody
    public String updateMusicFile(int id, MultipartFile[] files, List<File> fileInfos, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (files == null || files.length == 0) {
                res.setErrorMessage(I18nHelper.getMessage("file.empty"));
                return res.toJson();
            }

            //检测数据是否合法
            if (!musicService.checkMusicUploadFile(id, fileInfos)) {
                res.setErrorMessage(I18nHelper.getMessage("music.crud.file_number.error"));
                return res.toJson();
            }

            musicService.updateMusicFile(id, files, fileInfos, userService.getUserByRequest(request));
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

    //删除音频文件
    @RequestMapping(path = "/delete-file", method = RequestMethod.POST)
    @ResponseBody
    public String deleteMusicFile(@RequestBody MusicDeleteFileCmd cmd) {
        ApiResult res = new ApiResult();
        try {
            int id = cmd.getId();
            List<File> files = cmd.getFiles();
            res.message = musicService.deleteMusicFiles(id, files);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

}
