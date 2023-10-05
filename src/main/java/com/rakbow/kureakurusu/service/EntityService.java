package com.rakbow.kureakurusu.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.controller.interceptor.TokenInterceptor;
import com.rakbow.kureakurusu.dao.*;
import com.rakbow.kureakurusu.data.*;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.data.vo.album.AlbumVOAlpha;
import com.rakbow.kureakurusu.data.vo.book.BookVOBeta;
import com.rakbow.kureakurusu.data.vo.disc.DiscVOAlpha;
import com.rakbow.kureakurusu.data.vo.game.GameVOAlpha;
import com.rakbow.kureakurusu.entity.Album;
import com.rakbow.kureakurusu.entity.Book;
import com.rakbow.kureakurusu.entity.Disc;
import com.rakbow.kureakurusu.entity.Game;
import com.rakbow.kureakurusu.entity.view.MusicAlbumView;
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
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-02-18 17:30
 * @Description:
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
    private MusicMapper musicMapper;
    @Resource
    private EntryMapper entryMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private LikeUtil likeUtil;
    @Resource
    private EntityMapper entityMapper;
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
    private final MerchVOMapper merchVOMapper = MerchVOMapper.INSTANCES;
    private final MusicVOMapper musicVOMapper = MusicVOMapper.INSTANCES;

    private final List<Integer> searchEntityTypes = new ArrayList<>(){{
        add(Entity.ALBUM.getId());
        add(Entity.BOOK.getId());
        add(Entity.DISC.getId());
        add(Entity.GAME.getId());
//        add(Entity.MERCH.getId());
        add(Entity.MUSIC.getId());
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

    /**
     * 获取页面数据
     * @param entityType,entityId,addedTime,editedTime 实体类型，实体id,收录时间,编辑时间
     * @Author Rakbow
     */
    public pageInfo getPageInfo(int entityType, int entityId, Object entity) {

        JSONObject json = JSON.parseObject(JSON.toJSONString(entity));

        Timestamp addedTime = new Timestamp(json.getDate("addedTime").getTime());
        Timestamp editedTime = new Timestamp(json.getDate("editedTime").getTime());

        pageInfo pageInfo = new pageInfo();

        // 从cookie中获取点赞token
        String likeToken = TokenInterceptor.getLikeToken();
        if(likeToken == null) {
            pageInfo.setLiked(false);
        }else {
            pageInfo.setLiked(likeUtil.isLike(entityType, entityId, likeToken));
        }

        // 从cookie中获取访问token
        String visitToken = TokenInterceptor.getVisitToken();

        pageInfo.setAddedTime(DateHelper.timestampToString(addedTime));
        pageInfo.setEditedTime(DateHelper.timestampToString(editedTime));
        pageInfo.setVisitCount(visitUtil.incVisit(entityType, entityId, visitToken));
        pageInfo.setLikeCount(likeUtil.getLike(entityType, entityId));

        return pageInfo;
    }

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
            if(entityType == Entity.ALBUM.getId()) {
                List<AlbumVOAlpha> items = albumVOMapper.toVOAlpha(albumMapper.getAlbums(ids));

                for (AlbumVOAlpha item : items) {
                    item.setVisitNum(visits.get(item.getId()));
                }
                return JSON.parseArray(JSON.toJSONString(items));
            }
            if(entityType == Entity.BOOK.getId()) {
                List<BookVOBeta> items = bookVOMapper.book2VOBeta(bookMapper.getBooks(ids));

                for (BookVOBeta item : items) {
                    item.setVisitNum(visits.get(item.getId()));
                }
                return JSON.parseArray(JSON.toJSONString(items));
            }
            if(entityType == Entity.DISC.getId()) {
                List<DiscVOAlpha> items = discVOMapper.disc2VOAlpha(discMapper.getDiscs(ids));

                for (DiscVOAlpha item : items) {
                    item.setVisitNum(visits.get(item.getId()));
                }
                return JSON.parseArray(JSON.toJSONString(items));
            }
            if(entityType == Entity.GAME.getId()) {
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
        if(entityType == Entity.ALBUM.getId()) {
            return JSON.parseArray(JSON.toJSONString(albumVOMapper.toVOBeta(albumMapper.getAlbumOrderByAddedTime(limit))));
        }
        if(entityType == Entity.BOOK.getId()) {
            return JSON.parseArray(JSON.toJSONString(bookVOMapper.book2VOBeta(bookMapper.getBooksOrderByAddedTime(limit))));
        }
        if(entityType == Entity.DISC.getId()) {
            return JSON.parseArray(JSON.toJSONString(discVOMapper.disc2VOBeta(discMapper.getDiscsOrderByAddedTime(limit))));
        }
        if(entityType == Entity.GAME.getId()) {
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

        albumMapper.getAlbums(new ArrayList<>(){{
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
     * 更新数据库实体激活状态
     * @param tableName,entityId,status 实体表名,实体id,状态
     * @author rakbow
     */
    public void updateItemStatus(String tableName, int entityId, int status) {
        entityMapper.updateItemStatus(tableName, entityId, status);
    }

    /**
     * 批量更新数据库实体激活状态
     * @param tableName,ids,status 实体表名,ids,状态
     * @author rakbow
     */
    public void updateItemsStatus(String tableName, List<Integer> ids, int status) {
        entityMapper.updateItemsStatus(tableName, ids, status);
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
        entityMapper.updateItemDescription(tableName, entityId, description, DateHelper.NOW_TIMESTAMP);
    }

    /**
     * 更新特典信息
     *
     * @param tableName,entityId 实体表名,实体id
     * @param bonus 特典json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateItemBonus(String tableName, int entityId, String bonus) {
        entityMapper.updateItemBonus(tableName, entityId, bonus, DateHelper.NOW_TIMESTAMP);
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
        entityMapper.updateItemSpecs(tableName, entityId, specs, DateHelper.NOW_TIMESTAMP);
    }

    /**
     * 更新关联企业信息
     *
     * @param tableName,entityId 实体表名,实体id
     * @param companies 关联企业json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateItemCompanies(String tableName, int entityId, String companies) {
        entityMapper.updateItemCompanies(tableName, entityId, companies, DateHelper.NOW_TIMESTAMP);
    }

    /**
     * 更新相关人员信息
     *
     * @param tableName,entityId 实体表名,实体id
     * @param personnel 相关人员json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateItemPersonnel(String tableName, String fieldName, int entityId, String personnel) {
        entityMapper.updateItemPersonnel(tableName, fieldName, entityId, personnel, DateHelper.NOW_TIMESTAMP);
    }

    //endregion

    //region image operation

    /**
     * 根据实体类型和实体Id获取图片
     *
     * @param tableName,entityId 实体表名 实体id
     * @return JSONArray
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<Image> getItemImages(String tableName, int entityId) {
        return JSON.parseArray(entityMapper.getItemImages(tableName, entityId)).toJavaList(Image.class);
    }

    /**
     * 新增图片
     *
     * @param entityId           实体id
     * @param images             新增图片文件数组
     * @param originalImagesJson 数据库中现存的图片json数据
     * @param newImageInfos         新增图片json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public ActionResult addItemImages(String tableName, int entityId, MultipartFile[] images, List<Image> originalImagesJson,
                                      List<Image> newImageInfos) {
        ActionResult res = new ActionResult();
        try{
            ActionResult ar = qiniuImageUtil.commonAddImages(entityId, tableName, images, originalImagesJson, newImageInfos);
            if(ar.state) {
                JSONArray finalImageJson = JSON.parseArray(JSON.toJSONString(ar.data));
                entityMapper.updateItemImages(tableName, entityId, finalImageJson.toJSONString(), DateHelper.NOW_TIMESTAMP);
                res.message = ApiInfo.INSERT_IMAGES_SUCCESS;
            }else {
                throw new Exception(ar.message);
            }
        }catch(Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return res;
    }

    /**
     * 更新图片
     *
     * @param entityId     图书id
     * @param images 需要更新的图片json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String updateItemImages(String tableName, int entityId, String images) {
        entityMapper.updateItemImages(tableName, entityId, images, DateHelper.NOW_TIMESTAMP);
        return ApiInfo.UPDATE_IMAGES_SUCCESS;
    }

    /**
     * 删除图片
     *
     * @param tableName,entityId,images,deleteImages 实体表名,实体id,原图片信息,删除图片
     * @param deleteImages 需要删除的图片jsonArray
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String deleteItemImages(String tableName, int entityId, JSONArray deleteImages) throws Exception {

        JSONArray images = JSON.parseArray(JSON.toJSONString(getItemImages(tableName, entityId)));

        JSONArray finalImageJson = qiniuFileUtil.commonDeleteFiles(images, deleteImages);

        entityMapper.updateItemImages(tableName, entityId, finalImageJson.toString(), DateHelper.NOW_TIMESTAMP);
        return ApiInfo.DELETE_IMAGES_SUCCESS;
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

        if(entityType == Entity.ALBUM.getId()) {
            List<Album> albums = albumMapper.simpleSearch(keyword, limit, offset);
            if(!albums.isEmpty()) {
                res.setData(JSON.parseArray(JSON.toJSONString(albumVOMapper.toVOGamma(albums))));
                res.setTotal(albumMapper.simpleSearchCount(keyword));
            }
        }
        if(entityType == Entity.BOOK.getId()) {
            List<Book> books = bookMapper.simpleSearch(keyword, limit, offset);
            if(!books.isEmpty()) {
                res.setData(JSON.parseArray(JSON.toJSONString(bookVOMapper.book2VOGamma(books))));
                res.setTotal(bookMapper.simpleSearchCount(keyword));
            }
        }
        if(entityType == Entity.DISC.getId()) {
            List<Disc> discs = discMapper.simpleSearch(keyword, limit, offset);
            if(!discs.isEmpty()) {
                res.setData(JSON.parseArray(JSON.toJSONString(discVOMapper.disc2VOGamma(discs))));
                res.setTotal(discMapper.simpleSearchCount(keyword));
            }
        }
        if(entityType == Entity.GAME.getId()) {
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
       if(entityType == Entity.MUSIC.getId()) {
           List<MusicAlbumView> musics = musicMapper.simpleSearch(keyword, limit, offset);
           if(!musics.isEmpty()) {
               res.setData(JSON.parseArray(JSON.toJSONString(musicVOMapper.music2VOBeta(musics))));
               res.setTotal(musicMapper.simpleSearchCount(keyword));
           }
       }
        return res;
    }

    //endregion

}
