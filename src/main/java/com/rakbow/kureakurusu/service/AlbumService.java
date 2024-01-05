package com.rakbow.kureakurusu.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.controller.interceptor.AuthorityInterceptor;
import com.rakbow.kureakurusu.dao.AlbumMapper;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.bo.AlbumDiscBO;
import com.rakbow.kureakurusu.data.dto.AlbumDiscDTO;
import com.rakbow.kureakurusu.data.dto.AlbumTrackDTO;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.emun.common.MediaFormat;
import com.rakbow.kureakurusu.data.emun.entity.album.AlbumFormat;
import com.rakbow.kureakurusu.data.vo.album.AlbumVO;
import com.rakbow.kureakurusu.data.vo.album.AlbumVOBeta;
import com.rakbow.kureakurusu.entity.Album;
import com.rakbow.kureakurusu.entity.Music;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.*;
import com.rakbow.kureakurusu.util.convertMapper.entity.AlbumVOMapper;
import com.rakbow.kureakurusu.util.entity.AlbumUtil;
import com.rakbow.kureakurusu.util.file.QiniuFileUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2022-07-25 1:42 album业务层
 */
@Service
@RequiredArgsConstructor
public class AlbumService extends ServiceImpl<AlbumMapper, Album> {

    //region ------inject------
    private final AlbumMapper mapper;
    private final MusicService musicService;
    private final QiniuFileUtil qiniuFileUtil;
    private final VisitUtil visitUtil;
    private final AlbumVOMapper VOMapper = AlbumVOMapper.INSTANCES;

    private final int ENTITY_ALBUM_VALUE =  Entity.ALBUM.getValue();
    //endregion

    //region ------crud------

    // REQUIRED: 支持当前事务(外部事务),如果不存在则创建新事务.
    // REQUIRES_NEW: 创建一个新事务,并且暂停当前事务(外部事务).
    // NESTED: 如果当前存在事务(外部事务),则嵌套在该事务中执行(独立的提交和回滚),否则就会REQUIRED一样.

    /**
     * 根据Id获取Album,需要判断权限
     *
     * @param id id
     * @return Album
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
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
        try {

            List<Album> albums = mapper.selectBatchIds(ids);
            for (Album album : albums) {
                //删除前先把服务器上对应图片全部删除
                qiniuFileUtil.deleteAllImage(album.getImages());
                //删除专辑
                mapper.deleteById(album.getId());
                visitUtil.deleteVisit(ENTITY_ALBUM_VALUE, album.getId());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 更新专辑基础信息
     *
     * @param album 专辑
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateAlbum(Album album) {
        album.setEditedTime(DateHelper.now());
        mapper.updateById(album);
    }

    //endregion

    //region ------数据处理------

    public AlbumVO buildVO(Album album, List<Music> musics) {
        AlbumVO VO = VOMapper.toVO(album);

        if (AuthorityInterceptor.isJunior()) {
            //可供编辑的editDiscList
            VO.setEditDiscList(AlbumUtil.getEditDiscList(album.getTrackInfo(), musics));
            //可供编辑的editCompanies
            VO.setEditCompanies(JSON.parseArray(album.getCompanies()));
            //可供编辑的editPersonnel
            VO.setEditArtists(JSON.parseArray(album.getArtists()));
        }
        //音轨信息
        VO.setTrackInfo(AlbumUtil.getFinalTrackInfo(album.getTrackInfo(), musics));

        return VO;
    }

    //endregion

    //region ------更新复杂数据------

    /**
     * 更新音轨信息
     *
     * @param id        专辑id
     * @param _discList 专辑的音轨信息json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateAlbumTrackInfo(int id, String _discList) throws Exception {

        List<AlbumDiscDTO> albumDiscDTOs = JSON.parseArray(_discList).toJavaList(AlbumDiscDTO.class);

        //获取该专辑对应的音乐合集
        List<Music> musics = musicService.getMusicsByAlbumId(id);

        JSONObject trackInfo = new JSONObject();

        List<AlbumDiscBO> discList = new ArrayList<>();

        int discSerial = 1;

        String trackSerial;

        for (AlbumDiscDTO albumDiscDTO : albumDiscDTOs) {
            AlbumDiscBO albumDiscBO = new AlbumDiscBO();
            JSONArray trackList = new JSONArray();
            int i = 1;
            for (AlbumTrackDTO _track : albumDiscDTO.getTrackList()) {
                if (i < 10) {
                    trackSerial = "0" + i;
                } else {
                    trackSerial = Integer.toString(i);
                }
                _track.setSerial(trackSerial);

                //往music表中添加新数据
                //若musicId为0则代表该条数据为新增数据
                if (_track.getMusicId() == 0) {
                    Music music = new Music();
                    music.setName(_track.getName());
                    music.setAlbumId(id);
                    music.setDiscSerial(discSerial);
                    music.setTrackSerial(trackSerial);

                    //去除时间中含有的\t影响
                    String _time = _track.getLength();
                    if (_time.contains("\t")) {
                        music.setAudioLength(_time.replace("\t", ""));
                    } else {
                        music.setAudioLength(_time);
                    }

                    musicService.addMusic(music);
                    trackList.add(music.getId());
                } else {
                    //若musicId不为0则代表之前已经添加进music表中，需要进一步更新
                    Music currentMusic = DataFinder.findMusicById(_track.getMusicId(), musics);
                    assert currentMusic != null;
                    currentMusic.setName(_track.getName());
                    currentMusic.setAudioLength(_track.getLength());
                    currentMusic.setDiscSerial(discSerial);
                    currentMusic.setTrackSerial(trackSerial);
                    //更新对应music
                    musicService.updateMusic(currentMusic.getId(), currentMusic);
                    trackList.add(currentMusic.getId());
                    musics.remove(currentMusic);
                }

                i++;
            }

            albumDiscBO.setSerial(discSerial);
            albumDiscBO.setMediaFormat(MediaFormat.getIdsByNames(albumDiscDTO.getMediaFormat()));
            albumDiscBO.setAlbumFormat(AlbumFormat.getIdsByNames(albumDiscDTO.getAlbumFormat()));
            albumDiscBO.setTrackList(trackList);

            discSerial++;
            discList.add(albumDiscBO);
        }
        if (discList.size() != 0) {
            trackInfo.put("discList", discList);
        }
        LambdaUpdateWrapper<Album> updateWrapper = new LambdaUpdateWrapper<Album>()
                .eq(Album::getId, id)
                .set(Album::getTrackInfo, JSON.toJSONString(trackInfo))
                .set(Album::getEditedTime, DateHelper.now());

        mapper.update(null, updateWrapper);

        //删除对应music
        if (musics.size() != 0) {
            for (Music music : musics) {
                musicService.deleteMusic(music);
            }
        }
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
