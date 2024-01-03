package com.rakbow.kureakurusu.util.convertMapper.entity;

import com.alibaba.fastjson2.JSON;
import com.rakbow.kureakurusu.data.CommonConstant;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.emun.entity.music.AudioType;
import com.rakbow.kureakurusu.data.emun.temp.EnumUtil;
import com.rakbow.kureakurusu.entity.view.MusicAlbumView;
import com.rakbow.kureakurusu.data.vo.music.MusicVO;
import com.rakbow.kureakurusu.data.vo.music.MusicVOAlpha;
import com.rakbow.kureakurusu.data.vo.music.MusicVOBeta;
import com.rakbow.kureakurusu.entity.Music;
import com.rakbow.kureakurusu.util.common.*;
import com.rakbow.kureakurusu.util.entity.MusicUtil;
import com.rakbow.kureakurusu.util.file.QiniuImageUtil;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2023-01-12 10:45 Product VO转换接口
 */
@Mapper(componentModel = "spring")
public interface MusicVOMapper {

    MusicVOMapper INSTANCES = Mappers.getMapper(MusicVOMapper.class);

    /**
     * Music转VO对象，用于详情页面，转换量最大的
     *
     * @param music 音乐
     * @return MusicVO
     * @author rakbow
     */
    default MusicVO music2VO(Music music, String coverUrl) {
        if (music == null) {
            return null;
        }

        MusicVO musicVO = new MusicVO();

        musicVO.setId(music.getId());
        musicVO.setName(music.getName());
        musicVO.setNameEn(music.getNameEn());
        musicVO.setArtists(JSON.parseArray(music.getArtists()));
        musicVO.setDiscSerial(music.getDiscSerial());
        musicVO.setTrackSerial(music.getTrackSerial());

        musicVO.setAudioType(EnumUtil.getAttribute(AudioType.class, music.getAudioType()));

        musicVO.setFiles(JSON.parseArray(music.getFiles()));
        musicVO.setUploadDisabled(musicVO.getFiles().size() >= 2);

        if (StringUtils.isBlank(coverUrl)) {
            musicVO.setCover(QiniuImageUtil.getThumbUrl(CommonConstant.EMPTY_IMAGE_URL, 80));
        }else {
            musicVO.setCover(QiniuImageUtil.getThumbUrl(coverUrl, 80));
        }
        musicVO.setLrcText(music.getLrcText());
        musicVO.setAudioLength(music.getAudioLength());
        musicVO.setRemark(music.getRemark());

        return musicVO;
    }


    /**
     * Music转VO对象，用于list和index页面，转换量较少
     *
     * @param music 音乐
     * @return MusicVOAlpha
     * @author rakbow
     */
    default MusicVOAlpha music2VOAlpha(Music music, String coverUrl) {
        if (music == null) {
            return null;
        }

        MusicVOAlpha musicVOAlpha = new MusicVOAlpha();

        musicVOAlpha.setId(music.getId());
        musicVOAlpha.setName(music.getName());
        musicVOAlpha.setNameEn(music.getNameEn());
        musicVOAlpha.setDiscSerial(music.getDiscSerial());
        musicVOAlpha.setTrackSerial(music.getTrackSerial());

        if (StringUtils.isBlank(coverUrl)) {
            musicVOAlpha.setCover(QiniuImageUtil.getThumbUrl(CommonConstant.EMPTY_IMAGE_URL, 50));
        }else {
            musicVOAlpha.setCover(QiniuImageUtil.getThumbUrl(coverUrl, 50));
        }

        musicVOAlpha.setAudioLength(music.getAudioLength());

        musicVOAlpha.setAddedTime(DateHelper.timestampToString(music.getAddedTime()));
        musicVOAlpha.setEditedTime(DateHelper.timestampToString(music.getEditedTime()));

        return musicVOAlpha;
    }

    /**
     * 列表，Music转VO对象，用于list和index页面，转换量较少
     *
     * @param musics 音乐列表
     * @return List<MusicVOAlpha>
     * @author rakbow
     */
    default List<MusicVOAlpha> music2VOAlpha(List<Music> musics, String coverUrl) {
        List<MusicVOAlpha> musicVOAlphas = new ArrayList<>();
        if (!musics.isEmpty()) {
            musics.forEach(music -> musicVOAlphas.add(music2VOAlpha(music, coverUrl)));
        }
        return musicVOAlphas;
    }

    /**
     * 转VO对象，用于存储到搜索引擎
     *
     * @param musicAlbumView 音乐
     * @return MusicVOBeta
     * @author rakbow
     */
    default MusicVOBeta music2VOBeta(MusicAlbumView musicAlbumView) {
        if (musicAlbumView == null) {
            return null;
        }
        VisitUtil visitUtil = SpringUtil.getBean("visitUtil");
        LikeUtil likeUtil = SpringUtil.getBean("likeUtil");

        MusicVOBeta musicVOBeta = new MusicVOBeta();

        //基础信息
        musicVOBeta.setId(musicAlbumView.getId());
        musicVOBeta.setName(musicAlbumView.getName());
        musicVOBeta.setArtists(MusicUtil.getArtists(musicAlbumView));
        musicVOBeta.setAudioType(EnumUtil.getAttribute(AudioType.class, musicAlbumView.getAudioType()).getLabel());
        musicVOBeta.setAudioLength(musicAlbumView.getAudioLength());
        musicVOBeta.setHasLrc(musicAlbumView.getHasLrc() == 1);
        musicVOBeta.setHasFile(musicAlbumView.getHasFile() == 1);

        musicVOBeta.setAlbumId(musicAlbumView.getAlbumId());
        musicVOBeta.setAlbumName(musicAlbumView.getAlbumName());
        musicVOBeta.setCover(QiniuImageUtil.getThumb70Url(musicAlbumView.getAlbumImages()));

        musicVOBeta.setVisitCount(visitUtil.getVisit(Entity.MUSIC.getId(), musicAlbumView.getId()));
        musicVOBeta.setLikeCount(likeUtil.getLike(Entity.MUSIC.getId(), musicAlbumView.getId()));

        return musicVOBeta;
    }

    /**
     * 列表转换, 转VO对象，用于存储到搜索引擎
     *
     * @param musicAlbumViews 列表
     * @return List<MusicVOBeta>
     * @author rakbow
     */
    default List<MusicVOBeta> music2VOBeta(List<MusicAlbumView> musicAlbumViews) {
        List<MusicVOBeta> musicVOBetas = new ArrayList<>();

        if (!musicAlbumViews.isEmpty()) {
            musicAlbumViews.forEach(musicAlbumView -> musicVOBetas.add(music2VOBeta(musicAlbumView)));
        }

        return musicVOBetas;
    }
}
