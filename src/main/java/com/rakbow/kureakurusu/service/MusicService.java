package com.rakbow.kureakurusu.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.MusicMapper;
import com.rakbow.kureakurusu.data.ActionResult;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.emun.system.FileType;
import com.rakbow.kureakurusu.data.system.File;
import com.rakbow.kureakurusu.data.vo.music.MusicVOAlpha;
import com.rakbow.kureakurusu.data.entity.Music;
import com.rakbow.kureakurusu.data.entity.User;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.common.VisitUtil;
import com.rakbow.kureakurusu.util.convertMapper.entity.MusicVOMapper;
import com.rakbow.kureakurusu.util.file.QiniuBaseUtil;
import com.rakbow.kureakurusu.util.file.QiniuFileUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Rakbow
 * @since 2022-11-06 2:04 music业务层
 */
@RequiredArgsConstructor
@Service
public class MusicService extends ServiceImpl<MusicMapper, Music> {

    //region ------引入实例------
    private final MusicMapper mapper;
    private final QiniuBaseUtil qiniuBaseUtil;
    private final QiniuFileUtil qiniuFileUtil;
    private final VisitUtil visitUtil;
    private final MusicVOMapper musicVOMapper = MusicVOMapper.INSTANCES;
    //endregion

    //region -----曾删改查------

    /**
     * 根据专辑id获取该专辑所有音乐
     * @author rakbow
     * @param albumId 专辑id
     * @return list
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<Music> getMusicsByAlbumId(long albumId) {
        return mapper.selectList(new LambdaQueryWrapper<Music>().eq(Music::getAlbumId, albumId));
    }

    /**
     * 从专辑数据中新增music
     * @author rakbow
     * @param albumJson albumJson
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void addMusicsFromAlbum(JSONObject albumJson) throws Exception {

        int albumId = albumJson.getInteger("id");

        JSONObject trackInfo = albumJson.getJSONObject("trackInfo");

        if (trackInfo == null || Objects.equals(trackInfo.toJSONString(), "{}")) {
            return;
        }

        JSONObject cover = albumJson.getJSONObject("cover");

        JSONArray discList = trackInfo.getJSONArray("disc_list");

        for (int i = 0; i < discList.size(); i++) {
            JSONObject disc = discList.getJSONObject(i);
            JSONArray trackList = disc.getJSONArray("track_list");
            for (int j = 0; j < trackList.size(); j++) {
                JSONObject track = trackList.getJSONObject(j);
                Music music = new Music();
                music.setName(track.getString("name"));
                music.setAlbumId(albumId);
                music.setDiscSerial(disc.getInteger("serial"));
                music.setTrackSerial(track.getString("serial"));
                music.setAudioLength(track.getString("audioLength"));
                music.setAddedTime(DateHelper.stringToTimestamp(albumJson.getString("addedTime")));
                music.setEditedTime(DateHelper.stringToTimestamp(albumJson.getString("editedTime")));
                save(music);
            }
        }

    }

    /**
     * 更新music基础信息
     * @author rakbow
     * @param id,music music的id和music */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String updateMusic(int id, Music music) throws Exception {
        try {
            mapper.updateById(music);
        }catch (Exception ex) {
            throw new Exception(ex);
        }
        return I18nHelper.getMessage("entity.curd.update.success", Entity.MUSIC.getName());
    }

    /**
     * 根据id删除对应的music
     * @author rakbow
     * @param music music
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void deleteMusic(Music music) throws Exception {
        try {
            //删除对应music的音频文件
            deleteMusicAllFiles(music);
            mapper.deleteById(music.getId());
            visitUtil.deleteVisit(Entity.MUSIC.getValue(), music.getId());
        }catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    /**
     * 根据专辑id删除对应的music
     * @author rakbow
     * @param albumId 专辑id
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void deleteMusicByAlbumId(int albumId) throws Exception {
        try {
            List<Music> musics = getMusicsByAlbumId(albumId);
            //删除对应music的音频文件
            musics.forEach(this::deleteMusicAllFiles);
            musics.forEach(music -> visitUtil.deleteVisit(Entity.MUSIC.getValue(), music.getId()));
            mapper.delete(new LambdaQueryWrapper<Music>().eq(Music::getAlbumId, albumId));
        }catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    /**
     * 根据专辑id删除对应的music
     * @author rakbow
     * @param ids 专辑ids
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void deleteMusicsByAlbumIds(List<Long> ids) throws Exception {
        try {
            List<Music> musics = mapper.selectList(new LambdaQueryWrapper<Music>().in(Music::getAlbumId, ids));

            if(musics.isEmpty()) {
                return;
            }

            musics.forEach(music -> {
                //删除对应music的音频文件
                deleteMusicAllFiles(music);
                //删除浏览量数据
                visitUtil.deleteVisit(Entity.MUSIC.getValue(), music.getId());
                //删除对应music
                mapper.deleteById(music.getId());
            });
        }catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    //endregion

    //region ------处理数据------

    /**
     * 检测music数据
     * @author rakbow
     * @param musicJson musicJson
     * @return 错误信息
     * */
    public String checkMusicJson(JSONObject musicJson) {
        if (StringUtils.isBlank(musicJson.getString("name"))) {
            return I18nHelper.getMessage("entity.crud.name.required_field");
        }
        if (StringUtils.isBlank(musicJson.getString("audioType"))) {
            return I18nHelper.getMessage("music.crud.audio_type.required_field");
        }
        if (StringUtils.isBlank(musicJson.getString("audioLength"))) {
            return I18nHelper.getMessage("music.crud.audio_length.required_field");
        }
        return "";
    }

    //endregion

    //region ------特殊查询------

    /**
     * 获取同属一张碟片的音频
     * @author rakbow
     * @param music 音乐
     * @return list
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<MusicVOAlpha> getRelatedMusics(Music music, String coverUrl) {

        // //获取同属一张专辑的音频
        // List<Music> sameAlbumMusics = getMusicsByAlbumId(music.getAlbumId());
        //
        // if (sameAlbumMusics.size() >= 5) {
        //     sameAlbumMusics = sameAlbumMusics.subList(0, 5);
        // }
        //
        // List<Music> tmpList = DataFinder.findMusicByDiscSerial(music.getDiscSerial(), sameAlbumMusics);
        // for (int i = 0; i < tmpList.size(); i++) {
        //     if (tmpList.get(i).getId() == music.getId()) {
        //         tmpList.remove(tmpList.get(i));
        //     }
        // }
        //
        // //筛选出同一张碟片的音频，并按照序号排序
        // return musicVOMapper.music2VOAlpha(tmpList, coverUrl);
        return null;
    }

    //endregion


    //region -----更新数据------

    /**
     * 更新创作人员信息
     * @author rakbow
     * @param id 音乐id
     * @param artists 创作人员信息
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String updateMusicArtists(int id, String artists) {
        // mapper.updateMusicArtists(id, artists, DateHelper.now());
        return I18nHelper.getMessage("entity.crud.personnel.update.success");
    }

    /**
     * 更新歌词文本
     * @author rakbow
     * @param id 音乐id
     * @param lrcText 歌词文本
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String updateMusicLyricsText(int id, String lrcText) {
        // mapper.updateMusicLyricsText(id, lrcText, DateHelper.now());
        return I18nHelper.getMessage("music.crud.lyrics.update.success");
    }

    /**
     * 新增music文件
     *
     * @param id     id
     * @param files 新增文件数组
     * @param fileInfos 新增文件json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateMusicFile(int id, MultipartFile[] files, List<File> fileInfos, User user) throws IOException {

        //原数据库里的文件信息
        List<File> originalFiles = mapper.selectById(id).getFiles();

        //最终保存到数据库的json信息
        List<File> addFiles = new ArrayList<>();

        //创建存储链接前缀
        String filePath = "file/" + Entity.MUSIC.getTableName() + "/" + id + "/";

        for (int i = 0; i < files.length; i++) {

            ActionResult ar = new ActionResult();
            //判断文件是否为lrc歌词
            if (StringUtils.equals(files[i].getOriginalFilename().substring(files[i].getOriginalFilename().lastIndexOf(".")+1), "lrc")) {
                //上传lrc歌词文件
                ar = qiniuBaseUtil.uploadFileToQiniu(files[i], filePath, FileType.TEXT);
            }else {
                //上传音频文件
                ar = qiniuBaseUtil.uploadFileToQiniu(files[i], filePath, FileType.AUDIO);
            }
            if (ar.state) {
                File file = File.builder()
                        .url(ar.data.toString())
                        .name(fileInfos.get(i).getName())
                        .size(fileInfos.get(i).getSize())
                        .type(fileInfos.get(i).getType())
                        .uploadTime(DateHelper.nowStr())
                        .uploadUser(user.getUsername())
                        .build();
                addFiles.add(file);
            }
        }
        originalFiles.addAll(addFiles);
        // mapper.updateMusicFiles(id, originalFiles, DateHelper.now());
    }

    /**
     * 删除音频文件
     *
     * @param id           id
     * @param deleteFiles 需要删除的文件信息
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String deleteMusicFiles(int id, List<File> deleteFiles) throws Exception {
        //获取原始文件json数组
        List<File> orgFiles = mapper.selectById(id).getFiles();

        List<File> finalFileJson = qiniuFileUtil.deleteFile(orgFiles, deleteFiles);

        // mapper.updateMusicFiles(id, finalFileJson, DateHelper.now());
        return I18nHelper.getMessage("file.delete.success");
    }

    /**
     * 删除所有音频文件
     *
     * @param music music
     * @author rakbow
     */
    public void deleteMusicAllFiles(Music music) {
        //删除七牛云上的图片
        qiniuFileUtil.deleteAllFile(music.getFiles());
    }

    //endregion

    /**
     * 检测上传文件是否合法
     *
     * @param id     id
     * @param fileList 新增文件json数据
     * @author rakbow
     */
    public boolean checkMusicUploadFile(int id, List<File> fileList) {
        if (fileList.size() > 2) {
            return false;
        }

        //数据库中的文件信息
        List<File> files = mapper.selectById(id).getFiles();
        if(files.size() + fileList.size() > 2) {
            return false;
        }
        fileList.addAll(files);
        int lrcFileNum = 0;
        int audioFileNum = 0;
        for (File file : fileList) {
            //歌词文件数
            if (file.isText())
                lrcFileNum++;
            //音频文件数
            if (file.isAudio())
                audioFileNum++;
        }
        return lrcFileNum <= 1 && audioFileNum <= 1;
    }

}
