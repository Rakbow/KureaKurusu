package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.AlbumMapper;
import com.rakbow.kureakurusu.dao.EpisodeMapper;
import com.rakbow.kureakurusu.dao.ItemMapper;
import com.rakbow.kureakurusu.dao.PersonRelationMapper;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.SimpleSearchParam;
import com.rakbow.kureakurusu.data.dto.AlbumDetailQry;
import com.rakbow.kureakurusu.data.dto.AlbumListParams;
import com.rakbow.kureakurusu.data.dto.SearchQry;
import com.rakbow.kureakurusu.data.emun.DataActionType;
import com.rakbow.kureakurusu.data.emun.Entity;
import com.rakbow.kureakurusu.data.entity.Album;
import com.rakbow.kureakurusu.data.entity.Episode;
import com.rakbow.kureakurusu.data.entity.PersonRelation;
import com.rakbow.kureakurusu.data.vo.album.*;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.DataFinder;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.common.EntityUtil;
import com.rakbow.kureakurusu.util.common.VisitUtil;
import com.rakbow.kureakurusu.util.convertMapper.AlbumVOMapper;
import com.rakbow.kureakurusu.util.entity.EpisodeUtil;
import com.rakbow.kureakurusu.util.file.CommonImageUtil;
import com.rakbow.kureakurusu.util.file.QiniuImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
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
    private final QiniuImageUtil qiniuImageUtil;
    private final VisitUtil visitUtil;
    private final EntityUtil entityUtil;

    private final AlbumVOMapper VOMapper;
    private final AlbumMapper mapper;
    private final EpisodeMapper epMapper;
    private final PersonRelationMapper relationMapper;
    private final ItemMapper itemMapper;

    private final SqlSessionFactory sqlSessionFactory;

    private final int ENTITY_VALUE = Entity.ALBUM.getValue();
    //endregion

    //region crud

    // REQUIRED: 支持当前事务(外部事务),如果不存在则创建新事务.
    // REQUIRES_NEW: 创建一个新事务,并且暂停当前事务(外部事务).
    // NESTED: 如果当前存在事务(外部事务),则嵌套在该事务中执行(独立的提交和回滚),否则就会REQUIRED一样.

    @SneakyThrows
    @Transactional
    public AlbumDetailVO detail(AlbumDetailQry qry) {
        Album album = getById(qry.getId());
        if (album == null)
            throw new Exception(I18nHelper.getMessage("entity.url.error", Entity.ALBUM.getName()));
        String cover = CommonImageUtil.getCoverUrl(album.getImages());
        List<Episode> eps = epMapper.selectList(new LambdaQueryWrapper<Episode>().eq(Episode::getRelatedId, qry.getId()));

        return AlbumDetailVO.builder()
                .item(buildVO(album))
                .audios(EpisodeUtil.getAudios(eps, cover))
                .traffic(entityUtil.getPageTraffic(ENTITY_VALUE, qry.getId()))
                .options(entityUtil.getDetailOptions(ENTITY_VALUE))
                .itemImageInfo(CommonImageUtil.segmentImages(album.getImages(), 185, Entity.ALBUM, false))
                .build();
    }

    /**
     * 根据Id删除专辑
     *
     * @param ids 专辑ids
     * @author rakbow
     */
    @Transactional
    public void deleteAlbums(List<Long> ids) {
        //get original data
        List<Album> items = mapper.selectBatchIds(ids);
        for (Album item : items) {
            //delete all image
            qiniuImageUtil.deleteAllImage(item.getImages());
            //delete visit record
            visitUtil.deleteVisit(ENTITY_VALUE, item.getId());
        }
        //delete
        mapper.delete(new LambdaQueryWrapper<Album>().in(Album::getId, ids));
        //delete person relation
        relationMapper.delete(new LambdaQueryWrapper<PersonRelation>().eq(PersonRelation::getEntityType, ENTITY_VALUE).in(PersonRelation::getEntityId, ids));
        //delete related episode
        epMapper.delete(new LambdaQueryWrapper<Episode>().in(Episode::getRelatedId, ids));
    }

    //endregion

    //region data handle

    @Transactional
    public AlbumVO buildVO(Album album) {
        AlbumVO VO = VOMapper.toVO(album);
        //音轨信息
        VO.setTrackInfo(getTrackInfo(album));
        return VO;
    }

    @Transactional
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

    @Transactional
    public SearchResult<AlbumMiniVO> searchAlbums(SearchQry qry) {
        SimpleSearchParam param = new SimpleSearchParam(qry);
        if(param.keywordEmpty()) new SearchResult<>();

        LambdaQueryWrapper<Album> wrapper = new LambdaQueryWrapper<Album>()
                .or().like(Album::getName, param.getKeyword())
                .or().like(Album::getNameZh, param.getKeyword())
                .or().like(Album::getNameEn, param.getKeyword())
                .eq(Album::getStatus, 1)
                .orderByDesc(Album::getId);

        IPage<Album> pages = mapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);

        List<AlbumMiniVO> items = VOMapper.toMiniVO(pages.getRecords());

        return new SearchResult<>(items, pages.getTotal(), pages.getCurrent(), pages.getSize());
    }

    @Transactional
    public SearchResult<AlbumVOAlpha> getAlbums(AlbumListParams param) {

        QueryWrapper<Album> wrapper = new QueryWrapper<Album>()
                .like("name", param.getName())
                .like("name_zh", param.getNameZh())
                .like("name_en", param.getNameEn())
                .like("catalog_no", param.getCatalogNo())
                .like("ean13", param.getEan13())
                .in(CollectionUtils.isNotEmpty(param.getAlbumFormat()), "album_format", param.getAlbumFormat())
                .in(CollectionUtils.isNotEmpty(param.getPublishFormat()), "publish_format", param.getPublishFormat())
                .in(CollectionUtils.isNotEmpty(param.getMediaFormat()), "media_format", param.getMediaFormat())
                .orderBy(param.isSort(), param.asc(), param.sortField);

        IPage<Album> pages = mapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);
        List<AlbumVOAlpha> items = VOMapper.toVOAlpha(pages.getRecords());
        return new SearchResult<>(items, pages.getTotal(), pages.getCurrent(), pages.getSize());
    }

//    @Transactional
//    public SearchResult<AlbumVOAlpha> getAlbumsTest(AlbumListParams param) {
//        MPJLambdaWrapper<Item> wrapper = new MPJLambdaWrapper<Item>()
//                .selectAll(Item.class)
//                .selectAll(ItemAlbum.class)
//                .leftJoin(ItemAlbum.class, ItemAlbum::getId, Item::getEntityId)
//                .like(Item::getName, param.getName())
//                .like(Item::getNameZh, param.getNameZh())
//                .like(Item::getNameEn, param.getNameEn())
//                .like(ItemAlbum::getCatalogNo, param.getCatalogNo())
//                .like(ItemAlbum::getBarcode, param.getBarcode())
//                .in(CollectionUtils.isNotEmpty(param.getAlbumFormat()), ItemAlbum::getAlbumFormat, param.getAlbumFormat())
//                .in(CollectionUtils.isNotEmpty(param.getPublishFormat()), ItemAlbum::getPublishFormat, param.getPublishFormat())
//                .in(CollectionUtils.isNotEmpty(param.getMediaFormat()), ItemAlbum::getMediaFormat, param.getMediaFormat())
//                .orderBy(param.isSort(), param.asc(), param.sortField);
//        IPage<Album> pages = itemMapper.selectJoinPage(new Page<>(param.getPage(), param.getSize()), Album.class, wrapper);
//        List<AlbumVOAlpha> items = VOMapper.toVOAlpha(pages.getRecords());
//        return new SearchResult<>(items, pages.getTotal(), pages.getCurrent(), pages.getSize());
//    }

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
