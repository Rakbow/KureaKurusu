package com.rakbow.kureakurusu.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rakbow.kureakurusu.dao.*;
import com.rakbow.kureakurusu.data.*;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.vo.album.AlbumVOAlpha;
import com.rakbow.kureakurusu.data.vo.book.BookVOBeta;
import com.rakbow.kureakurusu.data.vo.disc.DiscVOAlpha;
import com.rakbow.kureakurusu.data.vo.game.GameVOAlpha;
import com.rakbow.kureakurusu.data.entity.Album;
import com.rakbow.kureakurusu.data.entity.Book;
import com.rakbow.kureakurusu.data.entity.Disc;
import com.rakbow.kureakurusu.data.entity.Game;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.common.LikeUtil;
import com.rakbow.kureakurusu.util.common.RedisUtil;
import com.rakbow.kureakurusu.util.common.VisitUtil;
import com.rakbow.kureakurusu.util.convertMapper.entity.*;
import com.rakbow.kureakurusu.util.entry.EntryUtil;
import com.rakbow.kureakurusu.util.file.CommonImageUtil;
import com.rakbow.kureakurusu.util.file.QiniuFileUtil;
import com.rakbow.kureakurusu.util.file.QiniuImageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Rakbow
 * @since 2023-02-18 17:30
 */
@Service
public class EntityService {

    //region instance

    @Resource
    private AlbumMapper albumMapper;
    @Resource
    private BookMapper bookMapper;
    @Resource
    private DiscMapper discMapper;
    @Resource
    private GameMapper gameMapper;
    @Resource
    private MerchMapper merchMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private LikeUtil likeUtil;
    @Resource
    private CommonMapper commonMapper;
    @Resource
    private QiniuImageUtil qiniuImageUtil;
    @Resource
    private QiniuFileUtil qiniuFileUtil;
    @Resource
    private VisitUtil visitUtil;
    @Resource
    private EntryUtil entryUtil;
    

    private final AlbumVOMapper albumVOMapper = AlbumVOMapper.INSTANCES;
    private final BookVOMapper bookVOMapper = BookVOMapper.INSTANCES;
    private final DiscVOMapper discVOMapper = DiscVOMapper.INSTANCES;
    private final GameVOMapper gameVOMapper = GameVOMapper.INSTANCES;

    private final List<Integer> searchEntityTypes = new ArrayList<>(){{
        add(Entity.ALBUM.getValue());
        add(Entity.BOOK.getValue());
        add(Entity.DISC.getValue());
        add(Entity.GAME.getValue());
//        add(Entity.MERCH.getId());
        add(Entity.EPISODE.getValue());
    }};

    //endregion

    /**
     * json对象转实体，以便保存到数据库
     *
     * @param json,entityClass json,entityClass
     * @return T
     * @author rakbow
     */
    public <T> T json2Entity(JSONObject json, Class<T> entityClass) {
        return JSON.to(entityClass, json);
    }

    //region common get data

//    /**
//     * 获取实体表数据
//     *
//     * @return JSONObject
//     * @author rakbow
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
//    public JSONObject getItemAmount() {
//        JSONObject entityAmounts = new JSONObject();
//        int total = 0;
//        for (Entity type : Entity.ENTITY_TYPES) {
//            Object amount;
//            try {
//                amount = redisUtil.get("entity_amount:" + type.getId());
//            }
//            catch (Exception e) {
//                amount = 0;
//            }
//            total += Integer.parseInt(amount.toString());
//            entityAmounts.put(type.getNameEn().toLowerCase() + "Amount", amount);
//        }
//        entityAmounts.put("Total", total);
//        return entityAmounts;
//    }

    /**
     * 获取浏览量最高的item
     *
     * @param limit 获取条数
     * @return list
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public JSONArray getPopularItems(int entityType, int limit) {
        LinkedHashMap<Integer, Long> visits = visitUtil.getEntityVisitRanking(entityType, limit);

        List<Integer> ids = new ArrayList<>(visits.keySet());

        if(!ids.isEmpty()) {
            if(entityType == Entity.ALBUM.getValue()) {
                List<AlbumVOAlpha> items = albumVOMapper.toVOAlpha(albumMapper.selectBatchIds(ids));

                for (AlbumVOAlpha item : items) {
                    item.setVisitNum(visits.get(item.getId()));
                }
                return JSON.parseArray(JSON.toJSONString(items));
            }
            if(entityType == Entity.BOOK.getValue()) {
                List<BookVOBeta> items = bookVOMapper.book2VOBeta(bookMapper.getBooks(ids));

                for (BookVOBeta item : items) {
                    item.setVisitNum(visits.get(item.getId()));
                }
                return JSON.parseArray(JSON.toJSONString(items));
            }
            if(entityType == Entity.DISC.getValue()) {
                List<DiscVOAlpha> items = discVOMapper.disc2VOAlpha(discMapper.getDiscs(ids));

                for (DiscVOAlpha item : items) {
                    item.setVisitNum(visits.get(item.getId()));
                }
                return JSON.parseArray(JSON.toJSONString(items));
            }
            if(entityType == Entity.GAME.getValue()) {
                List<GameVOAlpha> items = gameVOMapper.game2VOAlpha(gameMapper.getGames(ids));

                for (GameVOAlpha item : items) {
                    item.setVisitNum(visits.get(item.getId()));
                }
                return JSON.parseArray(JSON.toJSONString(items));
            }
//            if(entityType == Entity.MERCH.getId()) {
//                List<MerchVOAlpha> items = merchVOMapper.merch2VOAlpha(merchMapper.getMerchs(ids));
//
//                for (MerchVOAlpha item : items) {
//                    item.setVisitNum(visits.get(item.getId()));
//                }
//                return JSON.parseArray(JSON.toJSONString(items));
//            }
        }

        return null;
    }

    /**
     * 获取最新收录的item
     *
     * @param limit 获取条数
     * @return list封装的item
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public JSONArray getJustAddedItems(int entityType, int limit) {
        if(entityType == Entity.ALBUM.getValue()) {
            return JSON.parseArray(JSON.toJSONString(albumVOMapper.toVOBeta(
                    albumMapper.selectList(
                            new Page<>(1, limit),
                            new QueryWrapper<Album>().orderByAsc("added_time")
                    )
            )));
        }
        if(entityType == Entity.BOOK.getValue()) {
            return JSON.parseArray(JSON.toJSONString(bookVOMapper.book2VOBeta(bookMapper.getBooksOrderByAddedTime(limit))));
        }
        if(entityType == Entity.DISC.getValue()) {
            return JSON.parseArray(JSON.toJSONString(discVOMapper.disc2VOBeta(discMapper.getDiscsOrderByAddedTime(limit))));
        }
        if(entityType == Entity.GAME.getValue()) {
            return JSON.parseArray(JSON.toJSONString(gameVOMapper.game2VOBeta(gameMapper.getGamesOrderByAddedTime(limit))));
        }
//        if(entityType == Entity.MERCH.getId()) {
//            return JSON.parseArray(JSON.toJSONString(merchVOMapper.merch2VOAlpha(merchMapper.getMerchsOrderByAddedTime(limit))));
//        }
        return null;
    }

    //endregion

    //region refresh redis data

    /**
     * 刷新Redis缓存中的搜索页首页图片连接地址
     *
     * @author rakbow
     */
    public void refreshIndexCoverUrls () {

        JSONObject indexCoverUrl = new JSONObject();

        List<String> bookUrls = new ArrayList<>();
        List<String> albumUrls = new ArrayList<>();
        List<String> discUrls = new ArrayList<>();
        List<String> gameUrls = new ArrayList<>();
        List<String> merchUrls = new ArrayList<>();

        albumMapper.selectBatchIds(new ArrayList<>(){{
            add(11);add(13);add(109);add(10);add(6);
        }}).forEach(album -> albumUrls.add(QiniuImageUtil.getThumbUrl(CommonImageUtil.getCoverUrl(album.getImages()), 500)));

        bookMapper.getBooks(new ArrayList<>(){{
            add(148);add(173);add(121);add(18);add(36);
        }}).forEach(book -> bookUrls.add(QiniuImageUtil.getCustomThumbUrl(CommonImageUtil.getCoverUrl(book.getImages()), 240, 0)));

        discMapper.getDiscs(new ArrayList<>(){{
            add(114);add(57);add(58);add(59);add(1);
        }}).forEach(disc -> discUrls.add(QiniuImageUtil.getThumbBackgroundUrl(CommonImageUtil.getCoverUrl(disc.getImages()), 500)));

        gameMapper.getGames(new ArrayList<>(){{
            add(1);add(1);add(1);add(1);add(1);
        }}).forEach(game -> gameUrls.add(QiniuImageUtil.getThumbBackgroundUrl(CommonImageUtil.getCoverUrl(game.getImages()), 500)));

        merchMapper.getMerchs(new ArrayList<>(){{
            add(1);add(2);add(1);add(2);add(1);
        }}).forEach(merch -> merchUrls.add(QiniuImageUtil.getThumbBackgroundUrl(CommonImageUtil.getCoverUrl(merch.getImages()), 500)));

        indexCoverUrl.put("album", albumUrls);
        indexCoverUrl.put("book", bookUrls);
        indexCoverUrl.put("disc", discUrls);
        indexCoverUrl.put("game", gameUrls);
        indexCoverUrl.put("merch", merchUrls);

        redisUtil.set(RedisKey.INDEX_COVER_URL, indexCoverUrl);

    }

    //endregion

    //region common CRUD

    /**
     * 批量更新数据库实体激活状态
     * @param tableName,ids,status 实体表名,ids,状态
     * @author rakbow
     */
    public void updateItemsStatus(String tableName, List<Long> ids, int status) {
        commonMapper.updateItemStatus(tableName, ids, status);
    }

    /**
     * 点赞实体
     * @param entityType,entityId,likeToken 实体表名,实体id,点赞token
     * @author rakbow
     */
    public boolean entityLike(int entityType, int entityId, String likeToken) {
        //点过赞
        if(likeUtil.isLike(entityType, entityId, likeToken)) {
            return false;
        }else {//没点过赞,自增
            likeUtil.incLike(entityType, entityId, likeToken);
            return true;
        }
    }

    /**
     * 更新描述
     *
     * @param tableName,entityId 实体表名,实体id
     * @param description 描述json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateItemDescription(String tableName, int entityId, String description) {
        commonMapper.updateItemDetail(tableName, entityId, description, DateHelper.now());
    }

    /**
     * 更新规格信息
     *
     * @param tableName,entityId 实体表名,实体id
     * @param specs 规格json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateItemSpecs(String tableName, int entityId, String specs) {
        commonMapper.updateItemSpecs(tableName, entityId, specs, DateHelper.now());
    }

    //endregion

    //region search

    /**
     * 查询
     *
     * @param keyword 关键字
     * @author rakbow
     */
    public SimpleSearchResult simpleSearch(String keyword, int entityType, int offset, int limit) {
        // String entityName = EntityType.getItemNameEnByIndex(entityType).toLowerCase();

        SimpleSearchResult res = new SimpleSearchResult(keyword, entityType, Entity.getTableName(entityType), offset, limit);

        if (keyword.isEmpty()) {
            return res;
        }

        if (!searchEntityTypes.contains(entityType)) {
            return res;
        }

        QueryWrapper<Album> wrapper;
        long size = limit;
        long page = offset/size + 1;

        if(entityType == Entity.ALBUM.getValue()) {

            wrapper = new QueryWrapper<Album>()
                    .like("catalog_no", keyword)
                    .like("catalog_no", keyword)
                    .like("catalog_no", keyword)
                    .like("catalog_no", keyword)
                    .eq("status", 1)
                    .orderByDesc("added_time");

            IPage<Album> albums = albumMapper.selectPage(new Page<>(page, size), wrapper);

            if(albums.getTotal() != 0) {
                res.setData(JSON.parseArray(JSON.toJSONString(albumVOMapper.toVOGamma(albums.getRecords()))));
                res.setTotal(albums.getTotal());
            }
        }
        if(entityType == Entity.BOOK.getValue()) {
            List<Book> books = bookMapper.simpleSearch(keyword, limit, offset);
            if(!books.isEmpty()) {
                res.setData(JSON.parseArray(JSON.toJSONString(bookVOMapper.book2VOGamma(books))));
                res.setTotal(bookMapper.simpleSearchCount(keyword));
            }
        }
        if(entityType == Entity.DISC.getValue()) {
            List<Disc> discs = discMapper.simpleSearch(keyword, limit, offset);
            if(!discs.isEmpty()) {
                res.setData(JSON.parseArray(JSON.toJSONString(discVOMapper.disc2VOGamma(discs))));
                res.setTotal(discMapper.simpleSearchCount(keyword));
            }
        }
        if(entityType == Entity.GAME.getValue()) {
            List<Game> games = gameMapper.simpleSearch(keyword, limit, offset);
            if(!games.isEmpty()) {
                res.setData(JSON.parseArray(JSON.toJSONString(gameVOMapper.game2VOGamma(games))));
                res.setTotal(gameMapper.simpleSearchCount(keyword));
            }
        }
//        if(entityType == Entity.MERCH.getId()) {
//            List<Merch> merchs = merchMapper.simpleSearch(keyword, limit, offset);
//            if(!merchs.isEmpty()) {
//                res.setData(JSON.parseArray(JSON.toJSONString(merchVOMapper.merch2VOGamma(merchs))));
//                res.setTotal(merchMapper.simpleSearchCount(keyword));
//            }
//        }
       if(entityType == Entity.EPISODE.getValue()) {
           // List<MusicAlbumView> musics = musicMapper.simpleSearch(keyword, limit, offset);
           // if(!musics.isEmpty()) {
           //     res.setData(JSON.parseArray(JSON.toJSONString(musicVOMapper.music2VOBeta(musics))));
           //     res.setTotal(musicMapper.simpleSearchCount(keyword));
           // }
       }
        return res;
    }

    //endregion

}
