package com.rakbow.kureakurusu.util.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.CommonConstant;
import com.rakbow.kureakurusu.entity.view.MusicAlbumView;
import com.rakbow.kureakurusu.entity.Music;
import com.rakbow.kureakurusu.service.MusicService;
import com.rakbow.kureakurusu.util.file.QiniuImageUtil;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Rakbow
 * @since 2022-11-06 21:06
 */
public class MusicUtil {

    @Resource
    private static MusicService musicService;

    /**
     * 根据专辑id删除该专辑对应所有音乐
     * @author rakbow
     * @param albumId 专辑id
     * */
    public static void deleteAllMusicByAlbumId(int albumId) throws Exception {
        musicService.deleteMusicByAlbumId(albumId);
    }

    /**
     * 获取音频信息
     * @author rakbow
     * @param music 音乐
     * */
    public static JSONObject getMusicAudioInfo(Music music, String coverUrl) {
        JSONArray files = JSON.parseArray(music.getFiles());
        if (files.size() == 0) {
            return null;
        }
        JSONObject audioInfo = new JSONObject();
        for (int i = 0; i < files.size(); i++) {
            //判断是否有音频文件
            if (files.getJSONObject(i).getString("type").contains("audio")) {
                audioInfo.put("name", music.getName());
                audioInfo.put("artist", getArtists(music));
                audioInfo.put("url", files.getJSONObject(i).getString("url"));
                audioInfo.put("cover", QiniuImageUtil.getThumbUrl(CommonConstant.EMPTY_IMAGE_URL, 80));
                if (StringUtils.isBlank(coverUrl)) {
                    audioInfo.put("cover", QiniuImageUtil.getThumbUrl(CommonConstant.EMPTY_IMAGE_URL, 80));
                }else {
                    audioInfo.put("cover", QiniuImageUtil.getThumbUrl(coverUrl, 80));
                }
            }
            //判断是否有歌词文件
            if (files.getJSONObject(i).getString("type").contains("text")) {
                audioInfo.put("lrc", files.getJSONObject(i).getString("url"));
            }
        }
        return audioInfo;
    }

    /**
     * 获取音频信息
     * @author rakbow
     * @param musics 音乐
     * */
    public static JSONArray getMusicAudioInfo(List<Music> musics, String coverUrl) {
        if (musics.size() == 0) {
            return null;
        }
        JSONArray audioInfos = new JSONArray();
        musics.forEach(music -> {
            JSONObject audioInfo = getMusicAudioInfo(music, coverUrl);
            if (audioInfo != null) {
                audioInfos.add(audioInfo);
            }
        });
        if (audioInfos.size() == 0) {
            return null;
        }
        return audioInfos;
    }

    /**
     * 获取音频信息中的演唱者
     * @author rakbow
     * @param music 音乐
     * */
    public static String getArtists(Music music) {
        JSONArray artists = JSON.parseArray(music.getArtists());
        if (artists.size() == 0) {
            return "N/A";
        }
        for (int i = 0; i < artists.size(); i++) {
            if (artists.getJSONObject(i).getIntValue("main") == 1) {
                List<String> vocals = artists.getJSONObject(i).getList("name", String.class);
                return String.join("/", vocals);
            }
        }
        return "N/A";
    }

    /**
     * 获取音频信息中的演唱者
     * @author rakbow
     * @param musicAlbumView 音乐
     * */
    public static String getArtists(MusicAlbumView musicAlbumView) {
        JSONArray artists = JSON.parseArray(musicAlbumView.getArtists());
        if (artists.size() == 0) {
            return "N/A";
        }
        for (int i = 0; i < artists.size(); i++) {
            if (artists.getJSONObject(i).getIntValue("main") == 1) {
                List<String> vocals = artists.getJSONObject(i).getList("name", String.class);
                return String.join("/", vocals);
            }
        }
        return "N/A";
    }

    /**
     * 判断该音频信息是否包含附件
     * @author rakbow
     * @param music 音乐
     * */
    public static boolean hasFile(Music music) {
        JSONArray files = JSONArray.parseArray(music.getFiles());
        return files.size() != 0;
    }

    /**
     * 判断该音频信息是否有歌词
     * @author rakbow
     * @param music 音乐
     * */
    public static boolean hasLrc(Music music) {
        return !StringUtils.isBlank(music.getLrcText());
    }

    /**
     * 获取所属专辑ids
     * @author rakbow
     * @param musics 音乐
     * */
    public static List<Integer> getAlbumIds(List<Music> musics) {
        Set<Integer> tmpIds = new HashSet<>();

        musics.forEach(music -> tmpIds.add(music.getAlbumId()));

        return new ArrayList<>(tmpIds);
    }

}
