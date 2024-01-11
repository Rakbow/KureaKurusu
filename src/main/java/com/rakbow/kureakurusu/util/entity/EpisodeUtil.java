package com.rakbow.kureakurusu.util.entity;

import com.rakbow.kureakurusu.data.Audio;
import com.rakbow.kureakurusu.data.CommonConstant;
import com.rakbow.kureakurusu.data.entity.Episode;
import com.rakbow.kureakurusu.data.system.File;
import com.rakbow.kureakurusu.util.file.QiniuImageUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2022-11-06 21:06
 */
public class EpisodeUtil {

    /**
     * 获取音频信息
     * @author rakbow
     * */
    public static Audio getAudios(Episode ep, String cover) {
        List<File> files = ep.getFiles();
        if (files.isEmpty()) return null;
        Audio audio = new Audio();
        for (File file : files) {
            //判断是否有音频文件
            if(file.isAudio()) {
                audio = Audio.builder()
                        .title(file.getName())
                        .url(file.getUrl())
                        .cover(getAudioCover(cover))
                        .build();
            }
            //判断是否有歌词文件
            if (file.isText()) {
                audio.setLrc(file.getUrl());
            }
        }
        return audio;
    }

    private static String getAudioCover(String cover) {
        if (!StringUtils.isBlank(cover))
            return QiniuImageUtil.getThumbUrl(cover, 80);
        return QiniuImageUtil.getThumbUrl(CommonConstant.EMPTY_IMAGE_URL, 80);
    }

    /**
     * 获取音频信息
     * @author rakbow
     * @param episodes 音乐
     * */
    public static List<Audio> getAudios(List<Episode> episodes, String cover) {
        if (episodes.isEmpty()) return null;
        List<Audio> audios = new ArrayList<>();
        episodes.forEach(ep -> {
            Audio audio = getAudios(ep, cover);
            if (audio != null) audios.add(audio);
        });
        if (audios.isEmpty()) return null;
        return audios;
    }

    /**
     * 检测上传文件是否合法
     *
     * @param id     id
     * @param fileList 新增文件json数据
     * @author rakbow
     */
//    public boolean checkMusicUploadFile(int id, List<File> fileList) {
//        if (fileList.size() > 2) {
//            return false;
//        }
//
//        //数据库中的文件信息
//        List<File> files = mapper.selectById(id).getFiles();
//        if(files.size() + fileList.size() > 2) {
//            return false;
//        }
//        fileList.addAll(files);
//        int lrcFileNum = 0;
//        int audioFileNum = 0;
//        for (File file : fileList) {
//            //歌词文件数
//            if (file.isText())
//                lrcFileNum++;
//            //音频文件数
//            if (file.isAudio())
//                audioFileNum++;
//        }
//        return lrcFileNum <= 1 && audioFileNum <= 1;
//    }

}
