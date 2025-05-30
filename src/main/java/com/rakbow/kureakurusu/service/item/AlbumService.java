package com.rakbow.kureakurusu.service.item;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.*;
import com.rakbow.kureakurusu.data.emun.DataActionType;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.entity.Episode;
import com.rakbow.kureakurusu.data.entity.item.Item;
import com.rakbow.kureakurusu.data.entity.item.ItemAlbum;
import com.rakbow.kureakurusu.data.entity.resource.EntityFileRelated;
import com.rakbow.kureakurusu.data.entity.resource.FileInfo;
import com.rakbow.kureakurusu.data.vo.item.AlbumDiscVO;
import com.rakbow.kureakurusu.data.vo.item.AlbumTrackInfoVO;
import com.rakbow.kureakurusu.data.vo.item.AlbumTrackVO;
import com.rakbow.kureakurusu.service.ResourceService;
import com.rakbow.kureakurusu.toolkit.DataFinder;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * album业务层
 *
 * @author Rakbow
 * @since 2022-07-25 1:42
 */
@Service
@RequiredArgsConstructor
public class AlbumService extends ServiceImpl<ItemAlbumMapper, ItemAlbum> {

    //region inject

    private final EpisodeMapper epMapper;
    private final ItemAlbumMapper mapper;
    private final ItemMapper itemMapper;
    private final ResourceService resourceSrv;
    private final SqlSessionFactory sqlSessionFactory;
    private final EntityType ENTITY_TYPE = EntityType.ITEM;

    //endregion

    @Transactional
    public AlbumTrackInfoVO getTrackInfo(long id) {

        AlbumTrackInfoVO res = new AlbumTrackInfoVO();

        Item item = itemMapper.selectById(id);
        if (item == null) return res;

        //get all episode
        List<Episode> episodes = epMapper.selectList(
                new LambdaQueryWrapper<Episode>()
                        .eq(Episode::getRelatedType, ENTITY_TYPE.getValue())
                        .eq(Episode::getRelatedId, item.getId())
                        .orderByAsc(Episode::getDiscNo)
                        .orderByAsc(Episode::getSerial)
        );
        if (episodes.isEmpty()) return res;

        int totalDuration = 0;
        int discDuration;

        //grouping by Episode.discNo
        Map<Integer, List<Episode>> episodeGroup = episodes.stream()
                .collect(Collectors.groupingBy(Episode::getDiscNo));
        //build
        for (int discNo : episodeGroup.keySet()) {
            discDuration = 0;
            AlbumDiscVO disc = new AlbumDiscVO();
            disc.setSerial(discNo);
            disc.generateCode(item.getCatalogId(), discNo);
            for (Episode ep : episodeGroup.get(discNo)) {
                AlbumTrackVO track = AlbumTrackVO.builder()
                        .serial(ep.getSerial())
                        .id(ep.getId())
                        .title(ep.getTitle())
                        .titleEn(ep.getTitleEn())
                        .duration(DateHelper.getDuration(ep.getDuration()))
                        .action(DataActionType.NO_ACTION.getValue())
                        .build();
                disc.addTrack(track);
                discDuration += ep.getDuration();
            }
            disc.setDuration(DateHelper.getDuration(discDuration));
            totalDuration += discDuration;
            res.addDisc(disc);
        }
        res.setTotalTracks(episodes.size());
        res.setTotalDuration(DateHelper.getDuration(totalDuration));
        return res;
    }

    //endregion

    //region advance crud

    /**
     * 更新音轨信息
     *
     * @param id     专辑id
     * @param serial 专辑碟片序号
     * @param tracks 音轨信息
     * @author rakbow
     */
    @SneakyThrows
    @Transactional
    public void quickCreateAlbumDisc(long id, int serial, List<AlbumTrackVO> tracks) {

        int runTime = 0;
        int duration;

        List<Episode> eps = new ArrayList<>();
        for (AlbumTrackVO track : tracks) {
            Episode ep = new Episode();
            ep.setRelatedType(0);
            ep.setRelatedId(id);
            ep.setTitle(track.getTitle());
            ep.setSerial(track.getSerial());
            duration = DateHelper.getDuration(track.getDuration());
            runTime += duration;
            ep.setDuration(duration);
            ep.setDiscNo(serial);
            ep.setEpisodeType(0);
            eps.add(ep);
        }
        //save
        MybatisBatch.Method<Episode> method = new MybatisBatch.Method<>(EpisodeMapper.class);
        MybatisBatch<Episode> batchInsert = new MybatisBatch<>(sqlSessionFactory, eps);
        batchInsert.execute(method.insert());

        //update album track disc duration
        ItemAlbum album = mapper.selectById(id);
        int discNo = album.getDiscs() + 1;
        int trackNum = album.getTracks() + eps.size();
        runTime = album.getRunTime() + runTime;
        LambdaUpdateWrapper<ItemAlbum> wrapper = new LambdaUpdateWrapper<ItemAlbum>()
                .eq(ItemAlbum::getId, id)
                .set(ItemAlbum::getDiscs, discNo)
                .set(ItemAlbum::getTracks, trackNum)
                .set(ItemAlbum::getRunTime, runTime);
        mapper.update(null, wrapper);
    }

    /**
     * 更新音轨信息
     *
     * @param id    专辑id
     * @param discs 专辑的音轨信息
     * @author rakbow
     */
    @SneakyThrows
    @Transactional
    public void updateAlbumTrackInfo(long id, List<AlbumDiscVO> discs) {

        //get all episode
        List<Episode> episodes = epMapper.selectList(new LambdaQueryWrapper<Episode>().eq(Episode::getRelatedId, id));

        List<Episode> addEpSet = new ArrayList<>();
        List<Episode> updateEpSet = new ArrayList<>();
        List<Episode> deleteEpSet = new ArrayList<>();

        List<AlbumTrackVO> tracks = new ArrayList<>();
        for (AlbumDiscVO disc : discs) {

            tracks.clear();
            tracks.addAll(disc.getTracks());
            //insert
            if (disc.isAdd()) {
                //set serial
                IntStream.range(0, tracks.size()).forEach(i -> tracks.get(i).setSerial(i + 1));
                tracks.forEach(track -> {
                    Episode ep = Episode.builder()
                            .title(track.getTitle().replace("\t", ""))
                            .duration(DateHelper.getDuration(track.getDuration()))
                            .discNo(discs.indexOf(disc) + 1)
                            .serial(track.getSerial())
                            .relatedId(id)
                            .build();
                    addEpSet.add(ep);
                });
                continue;
            }
            //update or delete
            for (AlbumTrackVO track : tracks) {
                if (track.isUpdate()) {
                    Episode ep = DataFinder.findEpisodeById(track.getId(), episodes);
                    if (ep == null) continue;
                    ep.setEditedTime(DateHelper.now());
                    ep.setTitle(track.getTitle().replace("\t", ""));
                    ep.setDuration(DateHelper.getDuration(track.getDuration()));
                    ep.setSerial(track.getSerial());
                    updateEpSet.add(ep);
                } else if (track.isDelete()) {
                    Episode ep = DataFinder.findEpisodeById(track.getId(), episodes);
                    if (ep == null) continue;
                    deleteEpSet.add(ep);
                }
            }
        }
        //save
        MybatisBatch.Method<Episode> method = new MybatisBatch.Method<>(EpisodeMapper.class);
        MybatisBatch<Episode> batchInsert = new MybatisBatch<>(sqlSessionFactory, addEpSet);
        MybatisBatch<Episode> batchUpdate = new MybatisBatch<>(sqlSessionFactory, updateEpSet);
        MybatisBatch<Episode> batchDelete = new MybatisBatch<>(sqlSessionFactory, deleteEpSet);
        batchInsert.execute(method.insert());
        batchUpdate.execute(method.updateById());
        batchDelete.execute(method.deleteById());
    }

    //endregion

    @SneakyThrows
    @Transactional
    public void uploadAlbumTrackFiles(MultipartFile[] files, long albumId) {
        List<Episode> updateEps = new ArrayList<>();
        Tag tag;
        AudioFile audio;
        File file;
        Episode ep;
        int discNo;
        int serial;
        List<String> artists;
        List<String> composers;
        List<Episode> eps = epMapper.selectList(
                new LambdaQueryWrapper<Episode>()
                        .eq(Episode::getRelatedType, ENTITY_TYPE.getValue())
                        .eq(Episode::getRelatedId, albumId)
        );
        for (MultipartFile f : files) {
            file = FileUtil.convertToTempFile(f);
            audio = AudioFileIO.read(file);
            tag = audio.getTag();
            if (StringUtils.isBlank(tag.getFirst("DISCNO"))) {
                discNo = 1;
            } else {
                discNo = Integer.parseInt(tag.getFirst("DISCNO"));
            }
            serial = Integer.parseInt(tag.getFirst("TRACKNUMBER"));
            ep = DataFinder.findEpisodeByDiscNoAndSerial(discNo, serial, eps);
            if (ep == null) continue;
            artists = tag.getAll(FieldKey.ARTIST);
            composers = tag.getAll(FieldKey.COMPOSER);
            ep.setDetail(STR."Artists: \{String.join(", ", artists)}\nComposers: \{String.join(", ", composers)}\n");
            updateEps.add(ep);
            resourceSrv.uploadFileInfo(EntityType.EPISODE.getValue(), ep.getId(), file, Objects.requireNonNull(f.getOriginalFilename()));
        }
        MybatisBatch.Method<Episode> method = new MybatisBatch.Method<>(EpisodeMapper.class);
        MybatisBatch<Episode> batchUpdate = new MybatisBatch<>(sqlSessionFactory, updateEps);
        batchUpdate.execute(method.updateById());
    }

}
