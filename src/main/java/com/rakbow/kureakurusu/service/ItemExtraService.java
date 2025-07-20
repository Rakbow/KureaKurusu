package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.rakbow.kureakurusu.dao.*;
import com.rakbow.kureakurusu.data.dto.AlbumDiscCreateDTO;
import com.rakbow.kureakurusu.data.dto.AlbumTrackQuickUploadDTO;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.entity.Episode;
import com.rakbow.kureakurusu.data.entity.item.AlbumDisc;
import com.rakbow.kureakurusu.data.entity.item.ItemAlbum;
import com.rakbow.kureakurusu.data.entity.resource.FileInfo;
import com.rakbow.kureakurusu.data.entity.resource.FileRelated;
import com.rakbow.kureakurusu.data.vo.item.AlbumDiscVO;
import com.rakbow.kureakurusu.data.vo.item.AlbumTrackInfoVO;
import com.rakbow.kureakurusu.data.vo.item.AlbumTrackVO;
import com.rakbow.kureakurusu.exception.ApiException;
import com.rakbow.kureakurusu.toolkit.DataFinder;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.FileUtil;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.ISBNValidator;
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
import java.util.stream.Collectors;

/**
 * album业务层
 *
 * @author Rakbow
 * @since 2022-07-25 1:42
 */
@Service
@RequiredArgsConstructor
public class ItemExtraService {

    private final EpisodeMapper epMapper;
    private final ItemAlbumMapper albumMapper;
    private final AlbumDiscMapper discMapper;
    private final FileInfoMapper fileMapper;

    private final FileService fileSrv;

    private final SqlSessionFactory sqlSessionFactory;
    private final Converter converter;

    //region album

    @Transactional
    public AlbumTrackInfoVO getAlbumTracks(long id) {

        AlbumTrackInfoVO res = new AlbumTrackInfoVO();
        List<AlbumDisc> discs = discMapper.selectList(
                new LambdaQueryWrapper<AlbumDisc>().eq(AlbumDisc::getItemId, id).orderByAsc(AlbumDisc::getDiscNo)
        );
        if (discs.isEmpty()) return res;
        res.setDiscs(converter.convert(discs, AlbumDiscVO.class));
        //get all episode
        List<Episode> episodes = epMapper.selectList(
                new LambdaQueryWrapper<Episode>()
                        .eq(Episode::getRelatedType, EntityType.ALBUM_DISC)
                        .in(Episode::getRelatedId, discs.stream().map(AlbumDisc::getId).toList())
                        .orderByAsc(Episode::getSerial)
        );
        if (episodes.isEmpty()) return res;
        res.setTotalTracks(episodes.size());
        List<AlbumTrackVO> tracks = new ArrayList<>();
        episodes.forEach(e ->
                tracks.add(
                        AlbumTrackVO.builder()
                                .id(e.getId())
                                .discId(e.getRelatedId())
                                .serial(e.getSerial())
                                .name(e.getName())
                                .duration(DateHelper.getDuration(e.getDuration()))
                                .build()
                )
        );

        //build
        Map<Long, List<AlbumTrackVO>> discTrackMap = tracks.stream().collect(Collectors.groupingBy(AlbumTrackVO::getDiscId));
        int totalDuration = res.getDiscs().stream()
                .peek(d -> {
                    d.setTracks(discTrackMap.get(d.getId()));
                    d.setDuration(DateHelper.getDuration(
                            d.getTracks().stream().mapToInt(t -> DateHelper.getDuration(t.getDuration())).sum())
                    );
                })
                .mapToInt(d -> DateHelper.getDuration(d.getDuration()))
                .sum();

        res.setTotalDuration(DateHelper.getDuration(totalDuration));

        return res;
    }

    @SneakyThrows
    @Transactional
    public void albumTrackQuickCreate(AlbumDiscCreateDTO dto, boolean updateAlbum) {

        int runTime = 0;
        int duration;

        List<Episode> eps = new ArrayList<>();
        AlbumDisc disc = converter.convert(dto, AlbumDisc.class);
        discMapper.insert(disc);
        for (AlbumTrackVO track : dto.getTracks()) {
            Episode ep = new Episode();
            ep.setRelatedType(EntityType.ALBUM_DISC.getValue());
            ep.setRelatedId(disc.getId());
            ep.setName(track.getName());
            ep.setSerial(track.getSerial());
            duration = DateHelper.getDuration(track.getDuration());
            runTime += duration;
            ep.setDuration(duration);
            ep.setEpisodeType(0);
            eps.add(ep);
        }


        MybatisBatch.Method<Episode> epMethod = new MybatisBatch.Method<>(EpisodeMapper.class);
        MybatisBatch<Episode> epBatchInsert = new MybatisBatch<>(sqlSessionFactory, eps);
        epBatchInsert.execute(epMethod.insert());

        //update album track disc duration
        if (!updateAlbum) return;
        ItemAlbum album = albumMapper.selectById(dto.getItemId());
        int discNo = album.getDiscs() + 1;
        int trackNum = album.getTracks() + eps.size();
        runTime = album.getRunTime() + runTime;
        LambdaUpdateWrapper<ItemAlbum> wrapper = new LambdaUpdateWrapper<ItemAlbum>()
                .eq(ItemAlbum::getId, dto.getItemId())
                .set(ItemAlbum::getDiscs, discNo)
                .set(ItemAlbum::getTracks, trackNum)
                .set(ItemAlbum::getRunTime, runTime);
        albumMapper.update(null, wrapper);
    }

    @SneakyThrows
    @Transactional
    public void albumTrackQuickUpload(MultipartFile[] files, AlbumTrackQuickUploadDTO dto) {
        List<Episode> updateEps = new ArrayList<>();
        List<FileInfo> addFiles = new ArrayList<>();
        List<FileRelated> addFileRelatedList = new ArrayList<>();
        Tag tag;
        AudioFile audio;
        File file;
        Episode ep;
        List<String> artists;
        List<String> composers;
        StringBuilder epDetail = new StringBuilder();
        //get related episodes by album disc id
        List<Episode> eps = epMapper.selectList(
                new LambdaQueryWrapper<Episode>()
                        .eq(Episode::getRelatedType, EntityType.ALBUM_DISC.getValue())
                        .eq(Episode::getRelatedId, dto.getId())
        );
        String filePrefix = StringUtils.isNotBlank(dto.getDiscCatalogId()) ?
                dto.getDiscCatalogId() : STR."\{dto.getAlbumCatalogId()}_\{dto.getDiscNo()}";
        for (MultipartFile f : files) {
            file = FileUtil.convertToTempFile(f);
            audio = AudioFileIO.read(file);
            tag = audio.getTag();
            int trackNo = Integer.parseInt(tag.getFirst("TRACKNUMBER"));

            //find related episode
            ep = DataFinder.findEpisodeBySerial(trackNo, eps);
            if (ep == null) continue;

            //update episode detail
            artists = tag.getAll(FieldKey.ARTIST);
            composers = tag.getAll(FieldKey.COMPOSER);
            epDetail.setLength(0);
            if (!artists.isEmpty()) epDetail.append(STR."Artists: \{String.join(", ", artists)}\n\n");
            if (!composers.isEmpty()) epDetail.append(STR."Composers: \{String.join(", ", composers)}");
            ep.setDetail(epDetail.toString());
            updateEps.add(ep);

            FileRelated related = fileSrv.generateRelated(EntityType.EPISODE.getValue(), ep.getId(), file);
            addFileRelatedList.add(related);
            addFiles.add(related.getFileInfo());

            //update file name
            related.getFileInfo().setName(STR."\{filePrefix}_\{ep.getSerial()}.\{FileUtil.getExtension(f.getOriginalFilename())}");
        }

        MybatisBatch.Method<Episode> epMethod = new MybatisBatch.Method<>(EpisodeMapper.class);
        MybatisBatch.Method<FileRelated> fileRelatedMethod = new MybatisBatch.Method<>(FileRelatedMapper.class);

        MybatisBatch<Episode> epBatchUpdate = new MybatisBatch<>(sqlSessionFactory, updateEps);
        MybatisBatch<FileRelated> frBatchInsert = new MybatisBatch<>(sqlSessionFactory, addFileRelatedList);

        epBatchUpdate.execute(epMethod.updateById());
        addFiles.forEach(fileMapper::insert);

        addFileRelatedList.forEach(r -> r.setFileId(r.getFileInfo().getId()));
        frBatchInsert.execute(fileRelatedMethod.insert());
    }

    //endregion

    //region book

    /**
     * convert isbn-10 to isbn-13
     *
     * @param isbn10 isbn10
     * @return isbn13
     * @author rakbow
     */
    @SneakyThrows
    public String convertISBN(String isbn10) {

        ISBNValidator validator = new ISBNValidator();
        isbn10 = isbn10.replaceAll("-", "");
        if (validator.isValidISBN10(isbn10))
            throw new ApiException("book.crud.isbn10.invalid");
        return validator.convertToISBN13(isbn10);
    }

    //endregion

}
