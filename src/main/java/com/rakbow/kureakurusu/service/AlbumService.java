package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.controller.interceptor.AuthorityInterceptor;
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
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.*;
import com.rakbow.kureakurusu.util.convertMapper.entity.AlbumVOMapper;
import com.rakbow.kureakurusu.util.entity.EpisodeUtil;
import com.rakbow.kureakurusu.util.file.CommonImageUtil;
import com.rakbow.kureakurusu.util.file.QiniuFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
 * @author Rakbow
 * @since 2022-07-25 1:42 album业务层
 */
@Service
@RequiredArgsConstructor
public class AlbumService extends ServiceImpl<AlbumMapper, Album> {

    //region ------inject------
    private final QiniuFileUtil qiniuFileUtil;
    private final VisitUtil visitUtil;
    private final EntityUtil entityUtil;
    private final AlbumVOMapper VOMapper = AlbumVOMapper.INSTANCES;

    private final AlbumMapper mapper;
    private final EpisodeMapper epMapper;
    private final PersonRelationMapper relationMapper;

    private final SqlSessionFactory sqlSessionFactory;

    private final int ENTITY_VALUE = Entity.ALBUM.getValue();
    //endregion

    //region ------crud------

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
                .album(buildVO(album))
                .audios(AuthorityInterceptor.isUser() ? EpisodeUtil.getAudios(eps, cover) : null)
                .options(entityUtil.getDetailOptions(ENTITY_VALUE))
                .pageInfo(entityUtil.getPageTraffic(ENTITY_VALUE, qry.getId()))
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

    //region ------数据处理------

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

    //region ------更新复杂数据------

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

    //region ------特殊查询------

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public SearchResult getAlbums(QueryParams param) {

        //region wrapper

        // JSONArray products = param.getJSONArray("products");
        // JSONArray franchises = param.getJSONArray("franchises");
        // JSONArray publishFormat = param.getJSONArray("publishFormat");
        // JSONArray albumFormat = param.getJSONArray("albumFormat");
        // JSONArray mediaFormat = param.getJSONArray("mediaFormat");
        //
        // LambdaQueryWrapper<Album> wrapper = new LambdaQueryWrapper<Album>()
        //         .like(Album::getCatalogNo, param.getString("catalogNo"))
        //         .like(Album::getName, param.getString("name"))
        //         .like(Album::getNameZh, param.getString("nameZh"))
        //         .like(Album::getNameEn, param.getString("nameEn"));
        // if (products != null && products.isEmpty()) {
        //     wrapper.apply(String.format(CommonConstant.JSON_ARRAY_SEARCH_FORMAT, "products"), products.toJSONString());
        // }
        // if (franchises != null && franchises.isEmpty()) {
        //     wrapper.apply(String.format(CommonConstant.JSON_ARRAY_SEARCH_FORMAT, "franchises"), franchises.toJSONString());
        // }
        // if (publishFormat != null && publishFormat.isEmpty()) {
        //     wrapper.apply(String.format(CommonConstant.JSON_ARRAY_SEARCH_FORMAT, "publish_format"), publishFormat.toJSONString());
        // }
        // if (albumFormat != null && albumFormat.isEmpty()) {
        //     wrapper.apply(String.format(CommonConstant.JSON_ARRAY_SEARCH_FORMAT, "album_format"), albumFormat.toJSONString());
        // }
        // if (mediaFormat != null && mediaFormat.isEmpty()) {
        //     wrapper.apply(String.format(CommonConstant.JSON_ARRAY_SEARCH_FORMAT, "media_format"), mediaFormat.toJSONString());
        // }
        //
        // if (param.getBoolean("hasBonus") != null) {
        //     wrapper.eq(Album::getHasBonus, param.getBoolean("hasBonus") ? 1 : 0);
        // }
        // if (!AuthorityInterceptor.isSenior()) {
        //     wrapper.eq(Album::getStatus, 1);
        // }
        //
        // if (!StringUtils.isBlank(param.sortField)) {
        //     switch (param.sortField) {
        //         case "addedTime" -> wrapper.orderBy(true, param.sortOrder == 1, Album::getAddedTime);
        //         case "editedTime" -> wrapper.orderBy(true, param.sortOrder == 1, Album::getEditedTime);
        //         case "name" -> wrapper.orderBy(true, param.sortOrder == 1, Album::getName);
        //         case "nameZh" -> wrapper.orderBy(true, param.sortOrder == 1, Album::getNameZh);
        //         case "nameEn" -> wrapper.orderBy(true, param.sortOrder == 1, Album::getNameEn);
        //         case "catalogNo" -> wrapper.orderBy(true, param.sortOrder == 1, Album::getCatalogNo);
        //         case "barcode" -> wrapper.orderBy(true, param.sortOrder == 1, Album::getBarcode);
        //         case "releaseDate" -> wrapper.orderBy(true, param.sortOrder == 1, Album::getReleaseDate);
        //         case "price" -> wrapper.orderBy(true, param.sortOrder == 1, Album::getPrice);
        //         case "hasBonus" -> wrapper.orderBy(true, param.sortOrder == 1, Album::getHasBonus);
        //     }
        // }
        //
        // //endregion
        //
        // IPage<Album> albums = mapper.selectPage(new Page<>(param.page, param.size), wrapper);
        //
        // return new SearchResult(albums);
        return null;
    }

    /**
     * 获取相关联专辑
     *
     * @param id 专辑id
     * @return list封装的Album
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<AlbumVOBeta> getRelatedAlbums(int id) {

        List<Album> result = new ArrayList<>();

        // Album album = getAlbum(id);
        //
        // //该专辑包含的作品id
        // List<Integer> productIds = JSON.parseArray(album.getProducts()).toJavaList(Integer.class);
        //
        // //该系列所有专辑
        // List<Album> allAlbums = albumMapper.getAlbumsByFilter(null, null, CommonUtil.ids2List(album.getFranchises()),
        //         null, null, null, null, null, false, "releaseDate",
        //         1, 0, 0).stream().filter(tmpAlbum -> tmpAlbum.getId() != album.getId()).collect(Collectors.toList());
        //
        // List<Album> queryResult = allAlbums.stream().filter(tmpAlbum ->
        //         StringUtils.equals(tmpAlbum.getProducts(), album.getProducts())).collect(Collectors.toList());
        //
        // if (queryResult.size() > 5) {//结果大于5
        //     result.addAll(queryResult.subList(0, 5));
        // } else if (queryResult.size() == 5) {//结果等于5
        //     result.addAll(queryResult);
        // } else if (queryResult.size() > 0) {//结果小于5不为空
        //     List<Album> tmp = new ArrayList<>(queryResult);
        //
        //     if (productIds.size() > 1) {
        //         List<Album> tmpQueryResult = allAlbums.stream().filter(tmpAlbum ->
        //                 JSON.parseArray(tmpAlbum.getProducts()).toJavaList(Integer.class)
        //                         .contains(productIds.get(1))).collect(Collectors.toList());
        //
        //         if (tmpQueryResult.size() >= 5 - queryResult.size()) {
        //             tmp.addAll(tmpQueryResult.subList(0, 5 - queryResult.size()));
        //         } else if (tmpQueryResult.size() > 0 && tmpQueryResult.size() < 5 - queryResult.size()) {
        //             tmp.addAll(tmpQueryResult);
        //         }
        //     }
        //     result.addAll(tmp);
        // } else {
        //     List<Album> tmp = new ArrayList<>(queryResult);
        //     for (int productId : productIds) {
        //         tmp.addAll(
        //                 allAlbums.stream().filter(tmpAlbum ->
        //                         JSON.parseArray(tmpAlbum.getProducts()).toJavaList(Integer.class)
        //                                 .contains(productId)).collect(Collectors.toList())
        //         );
        //     }
        //     result = CommonUtil.removeDuplicateList(tmp);
        //     if (result.size() >= 5) {
        //         result = result.subList(0, 5);
        //     }
        // }

        return VOMapper.toVOBeta(CommonUtil.removeDuplicateList(result));
    }

    // /**
    //  * 根据作品id获取关联专辑
    //  *
    //  * @param productId 作品id
    //  * @return List<JSONObject>
    //  * @author rakbow
    //  */
    // @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    // public List<AlbumVOBeta> getAlbumsByProductId(int productId) {
    //
    //     List<Integer> products = new ArrayList<>();
    //     products.add(productId);
    //
    //     List<Album> albums = albumMapper.getAlbumsByFilter(null, null, null, products,
    //             null, null, null, null, false, "releaseDate",
    //             -1, 0, 0);
    //
    //     return albumVOMapper.toVOBeta(albums);
    // }

    //endregion

}
