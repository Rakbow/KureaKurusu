package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.AlbumMapper;
import com.rakbow.kureakurusu.dao.EpisodeMapper;
import com.rakbow.kureakurusu.dao.PersonRelationMapper;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.dto.album.AlbumDetailQry;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.emun.system.DataActionType;
import com.rakbow.kureakurusu.data.entity.Album;
import com.rakbow.kureakurusu.data.entity.Episode;
import com.rakbow.kureakurusu.data.entity.PersonRelation;
import com.rakbow.kureakurusu.data.vo.album.*;
import com.rakbow.kureakurusu.interceptor.AuthorityInterceptor;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.DataFinder;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.common.EntityUtil;
import com.rakbow.kureakurusu.util.common.VisitUtil;
import com.rakbow.kureakurusu.util.convertMapper.entity.AlbumVOMapper;
import com.rakbow.kureakurusu.util.entity.EpisodeUtil;
import com.rakbow.kureakurusu.util.file.CommonImageUtil;
import com.rakbow.kureakurusu.util.file.QiniuFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
public class AlbumService extends ServiceImpl<AlbumMapper, Album> {

    //region inject
    private final QiniuFileUtil qiniuFileUtil;
    private final VisitUtil visitUtil;
    private final EntityUtil entityUtil;

    private final AlbumVOMapper VOMapper;
    private final AlbumMapper mapper;
    private final EpisodeMapper epMapper;
    private final PersonRelationMapper relationMapper;

    private final SqlSessionFactory sqlSessionFactory;

    private final int ENTITY_VALUE = Entity.ALBUM.getValue();
    //endregion

    //region crud

    // REQUIRED: 支持当前事务(外部事务),如果不存在则创建新事务.
    // REQUIRES_NEW: 创建一个新事务,并且暂停当前事务(外部事务).
    // NESTED: 如果当前存在事务(外部事务),则嵌套在该事务中执行(独立的提交和回滚),否则就会REQUIRED一样.

    @SneakyThrows
    public AlbumDetailVO getDetail(AlbumDetailQry qry) {
        Album album = getAlbum(qry.getId());
        if (album == null)
            throw new Exception(I18nHelper.getMessage("entity.url.error", Entity.ALBUM.getName()));
        String cover = CommonImageUtil.getCoverUrl(album.getImages());
        List<Episode> eps = epMapper.selectList(new LambdaQueryWrapper<Episode>().eq(Episode::getRelatedId, qry.getId()));

        return AlbumDetailVO.builder()
                .item(buildVO(album))
                .audios(AuthorityInterceptor.isUser() ? EpisodeUtil.getAudios(eps, cover) : null)
                .traffic(entityUtil.getPageTraffic(ENTITY_VALUE, qry.getId()))
                .options(entityUtil.getDetailOptions(ENTITY_VALUE))
                .itemImageInfo(CommonImageUtil.segmentImages(album.getImages(), 185, Entity.ALBUM, false))
                .build();
    }

    /**
     * 根据Id获取Album,需要判断权限
     *
     * @param id id
     * @return Album
     * @author rakbow
     */
    public Album getAlbum(long id) {
        if (AuthorityInterceptor.isSenior()) {
            return mapper.selectOne(new LambdaQueryWrapper<Album>().eq(Album::getId, id));
        }
        return mapper.selectOne(new LambdaQueryWrapper<Album>().eq(Album::getStatus, 1).eq(Album::getId, id));
    }

    /**
     * 根据Id删除专辑
     *
     * @param ids 专辑ids
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void deleteAlbums(List<Long> ids) {
        //get original data
        List<Album> albums = mapper.selectBatchIds(ids);
        for (Album album : albums) {
            //delete all image
            qiniuFileUtil.deleteAllImage(album.getImages());
            //delete album
            mapper.deleteById(album.getId());
            //delete visit record
            visitUtil.deleteVisit(ENTITY_VALUE, album.getId());
            //delete person relation
            relationMapper.delete(new LambdaQueryWrapper<PersonRelation>().eq(PersonRelation::getEntityId, album.getId()));
        }
        //delete related episode
        epMapper.delete(new LambdaQueryWrapper<Episode>().in(Episode::getRelatedId, ids));
    }

    /**
     * 更新专辑基础信息
     *
     * @param album 专辑
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateAlbum(Album album) {
        update(
                new LambdaUpdateWrapper<Album>()
                        .eq(Album::getId, album.getId())
                        .set(Album::getName, album.getName())
                        .set(Album::getNameZh, album.getNameZh())
                        .set(Album::getNameEn, album.getNameEn())
                        .set(Album::getCatalogNo, album.getCatalogNo())
                        .set(Album::getBarcode, album.getBarcode())
                        .set(Album::getReleaseDate, album.getReleaseDate())
                        .set(Album::getPrice, album.getPrice())
                        .set(Album::getCurrencyUnit, album.getCurrencyUnit())
                        .set(Album::getHasBonus, album.getHasBonus())
                        .set(Album::getAlbumFormat, album.getAlbumFormat())
                        .set(Album::getMediaFormat, album.getMediaFormat())
                        .set(Album::getPublishFormat, album.getPublishFormat())
                        .set(Album::getRemark, album.getRemark())
                        .set(Album::getEditedTime, DateHelper.now())
        );
    }

    //endregion

    //region data handle

    public AlbumVO buildVO(Album album) {
        AlbumVO VO = VOMapper.toVO(album);
        //音轨信息
        VO.setTrackInfo(getTrackInfo(album));
        return VO;
    }

    public AlbumTrackInfoVO getTrackInfo(Album album) {
        AlbumTrackInfoVO res = new AlbumTrackInfoVO();
        //get all episode
        List<Episode> episodes = epMapper.selectList(
                new LambdaQueryWrapper<Episode>()
                        .eq(Episode::getRelatedId, album.getId())
                        .orderByAsc(Episode::getDiscNum)
                        .orderByAsc(Episode::getSerial)
        );
        if (episodes.isEmpty()) return res;

        int totalDuration = 0;
        int discDuration;

        //grouping by Episode.discNum
        Map<Integer, List<Episode>> episodeGroup = episodes.stream()
                .collect(Collectors.groupingBy(Episode::getDiscNum));
        //build
        for (int discNum : episodeGroup.keySet()) {
            discDuration = 0;
            AlbumDiscVO disc = new AlbumDiscVO();
            disc.setSerial(discNum);
            disc.generateCode(album.getCatalogNo(), discNum);
            for (Episode ep : episodeGroup.get(discNum)) {
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

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public SearchResult<AlbumVOAlpha> getAlbums(QueryParams param) {

        String name = param.getStr("name");
        String nameZh = param.getStr("nameZh");
        String nameEn = param.getStr("nameEn");
        String catalogNo = param.getStr("catalogNo");
        String barcode = param.getStr("barcode");
        LambdaQueryWrapper<Album> wrapper = new LambdaQueryWrapper<Album>()
                .like(StringUtils.isNotBlank(name), Album::getName, name)
                .like(StringUtils.isNotBlank(nameZh), Album::getNameZh, nameZh)
                .like(StringUtils.isNotBlank(nameEn), Album::getNameEn, nameEn)
                .like(StringUtils.isNotBlank(catalogNo), Album::getNameEn, catalogNo)
                .like(StringUtils.isNotBlank(barcode), Album::getNameEn, barcode);

        List<Integer> albumFormat = param.getArray("albumFormat");
        List<Integer> publishFormat = param.getArray("publishFormat");
        List<Integer> mediaFormat = param.getArray("mediaFormat");
        if(CollectionUtils.isNotEmpty(albumFormat))
            wrapper.in(Album::getAlbumFormat, albumFormat);
        if(CollectionUtils.isNotEmpty(publishFormat))
            wrapper.in(Album::getAlbumFormat, publishFormat);
        if(CollectionUtils.isNotEmpty(mediaFormat))
            wrapper.in(Album::getAlbumFormat, mediaFormat);

        if(StringUtils.isNotBlank(param.sortField)) {
            switch (param.sortField) {
                case "name" -> wrapper.orderBy(true, param.asc(), Album::getName);
                case "nameZh" -> wrapper.orderBy(true, param.asc(), Album::getNameZh);
                case "nameEn" -> wrapper.orderBy(true, param.asc(), Album::getNameEn);
                case "releaseDate" -> wrapper.orderBy(true, param.asc(), Album::getReleaseDate);
                case "barcode" -> wrapper.orderBy(true, param.asc(), Album::getBarcode);
                case "catalogNo" -> wrapper.orderBy(true, param.asc(), Album::getCatalogNo);
                case "price" -> wrapper.orderBy(true, param.asc(), Album::getPrice);
                case "addedTime" -> wrapper.orderBy(true, param.asc(), Album::getAddedTime);
                case "editedTime" -> wrapper.orderBy(true, param.asc(), Album::getEditedTime);
            }
        }else {
            wrapper.orderByDesc(Album::getId);
        }

        IPage<Album> pages = mapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);
        List<AlbumVOAlpha> items = VOMapper.toVOAlpha(pages.getRecords());

        return new SearchResult<>(items, pages.getTotal(), pages.getCurrent(), pages.getSize());
    }

    /**
     * 更新音轨信息
     *
     * @param id    专辑id
     * @param discs 专辑的音轨信息
     * @author rakbow
     */
    @SneakyThrows
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
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
                            .discNum(discs.indexOf(disc)+1)
                            .serial(track.getSerial())
                            .relatedId(id)
                            .build();
                    addEpSet.add(ep);
                });
                continue;
            }
            //update or delete
            for(AlbumTrackVO track : tracks) {
                if (track.isUpdate()) {
                    Episode ep = DataFinder.findEpisodeById(track.getId(), episodes);
                    if(ep == null) continue;
                    ep.setEditedTime(DateHelper.now());
                    ep.setTitle(track.getTitle().replace("\t", ""));
                    ep.setDuration(DateHelper.getDuration(track.getDuration()));
                    ep.setSerial(track.getSerial());
                    updateEpSet.add(ep);
                }else if(track.isDelete()) {
                    Episode ep = DataFinder.findEpisodeById(track.getId(), episodes);
                    if(ep == null) continue;
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

}
