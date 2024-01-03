//package com.rakbow.kureakurusu.util.common;
//
//import com.alibaba.fastjson2.JSON;
//import com.meilisearch.sdk.Client;
//import com.meilisearch.sdk.Config;
//import com.meilisearch.sdk.Index;
//import com.meilisearch.sdk.SearchRequest;
//import com.meilisearch.sdk.exceptions.MeilisearchException;
//import com.meilisearch.sdk.model.SearchResult;
//import com.rakbow.kureakurusu.data.MeiliSearchResult;
//import com.rakbow.kureakurusu.data.emun.common.Entity;
//import com.rakbow.kureakurusu.util.convertMapper.entity.*;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @Project_name: kureakurusu
// * @author Rakbow
// * @since 2023-02-03 13:13
// * @description MeiliSearch工具类
// */
//@Component
//public class MeiliSearchUtils {
//
//    @Value("${meilisearch.hostUrl}")
//    private String hostUrl;
//    @Value("${meilisearch.port}")
//    private String port;
//    @Value("${meilisearch.apiKey}")
//    private String apiKey;
//
//    private final AlbumVOMapper albumVOMapper = AlbumVOMapper.INSTANCES;
//    private final BookVOMapper bookVOMapper = BookVOMapper.INSTANCES;
//    private final DiscVOMapper discVOMapper = DiscVOMapper.INSTANCES;
//    private final GameVOMapper gameVOMapper = GameVOMapper.INSTANCES;
//    private final MerchVOMapper merchVOMapper = MerchVOMapper.INSTANCES;
//    private final ProductVOMapper productVOMapper = ProductVOMapper.INSTANCES;
//
//    private final List<Integer> searchEntityTypes = new ArrayList<>(){{
//        add(Entity.ALBUM.getId());
//        add(Entity.BOOK.getId());
//        add(Entity.DISC.getId());
//        add(Entity.GAME.getId());
//        add(Entity.MERCH.getId());
//    }};
//
//    // /**
//    //  * 存储单个数据到搜索引擎
//    //  *
//    //  * @param object,entityType 数据,实体类型
//    //  * @author rakbow
//    //  */
//    // public void saveSingleData(Object object, EntityType entityType) throws MeilisearchException {
//    //
//    //     Client client = new Client(new Config(hostUrl + ":" + port, apiKey));
//    //
//    //     Index index = client.index(entityType.getNameEn().toLowerCase());
//    //
//    //     if (entityType == EntityType.ALBUM) {
//    //         Album album = (Album) object;
//    //         index.addDocuments(JSON.toJSONString(albumVOMapper.album2VOGamma(album)));
//    //     }
//    //     if (entityType == EntityType.BOOK) {
//    //         Book book = (Book) object;
//    //         index.addDocuments(JSON.toJSONString(bookVOMapper.book2VOGamma(book)));
//    //     }
//    //     if (entityType == EntityType.DISC) {
//    //         Disc disc = (Disc) object;
//    //         index.addDocuments(JSON.toJSONString(discVOMapper.disc2VOGamma(disc)));
//    //     }
//    //     if (entityType == EntityType.GAME) {
//    //         Game game = (Game) object;
//    //         index.addDocuments(JSON.toJSONString(gameVOMapper.game2VOGamma(game)));
//    //     }
//    //     if (entityType == EntityType.MERCH) {
//    //         Merch merch = (Merch) object;
//    //         index.addDocuments(JSON.toJSONString(merchVOMapper.merch2VOGamma(merch)));
//    //     }
//    //     if (entityType == EntityType.PRODUCT) {
//    //         Product product = (Product) object;
//    //         index.addDocuments(JSON.toJSONString(productVOMapper.product2VOBeta(product)));
//    //     }
//    // }
//
//    // /**
//    //  * 存储批量数据到搜索引擎
//    //  *
//    //  * @param object,entityType 数据,实体类型
//    //  * @author rakbow
//    //  */
//    // public void saveMultiData(Object object, EntityType entityType) throws MeilisearchException {
//    //
//    //     Client client = new Client(new Config(hostUrl + ":" + port, apiKey));
//    //
//    //     Index index = client.index(entityType.getNameEn().toLowerCase());
//    //
//    //     JSONArray data = new JSONArray();
//    //
//    //     if (entityType == EntityType.ALBUM) {
//    //         List<Album> albums = (List<Album>) object;
//    //         albums.forEach(album -> data.add(albumVOMapper.album2VOGamma(album)));
//    //     }
//    //     if (entityType == EntityType.BOOK) {
//    //         List<Book> books = (List<Book>) object;
//    //         books.forEach(book -> data.add(bookVOMapper.book2VOGamma(book)));
//    //     }
//    //     if (entityType == EntityType.DISC) {
//    //         List<Disc> discs = (List<Disc>) object;
//    //         discs.forEach(disc -> data.add(discVOMapper.disc2VOGamma(disc)));
//    //     }
//    //     if (entityType == EntityType.GAME) {
//    //         List<Game> games = (List<Game>) object;
//    //         games.forEach(game -> data.add(gameVOMapper.game2VOGamma(game)));
//    //     }
//    //     if (entityType == EntityType.MERCH) {
//    //         List<Merch> merchs = (List<Merch>) object;
//    //         merchs.forEach(merch -> data.add(merchVOMapper.merch2VOGamma(merch)));
//    //     }
//    //     if (entityType == EntityType.PRODUCT) {
//    //         List<Product> products = (List<Product>) object;
//    //         products.forEach(product -> data.add(productVOMapper.product2VOBeta(product)));
//    //
//    //     }
//    //
//    //     index.addDocuments(data.toJSONString());
//    // }
//
//    /**
//     * 删除数据
//     *
//     * @param id,entityType 数据id,实体类型
//     * @author rakbow
//     */
//    public void deleteData(int id, Entity entity) throws MeilisearchException {
//        Client client = new Client(new Config(hostUrl + ":" + port, apiKey));
//
//        Index index = client.index(entity.getNameEn().toLowerCase());
//
//        index.deleteDocument(Integer.toString(id));
//    }
//
//    /**
//     * 查询
//     *
//     * @param keyword 关键字
//     * @author rakbow
//     */
//    public MeiliSearchResult simpleSearch(String keyword, int entityType, int offset, int limit) throws MeilisearchException {
//        Client client = new Client(new Config(hostUrl + ":" + port, apiKey));
//
//        MeiliSearchResult result = new MeiliSearchResult();
//
//        if (keyword.isEmpty()) {
//            return result;
//        }
//
//        if (!searchEntityTypes.contains(entityType)) {
//            return result;
//        }
//
//        SearchRequest request = new SearchRequest(keyword, offset, limit);
//
//        SearchResult results = client.index(Entity.getItemNameEnByIndex(entityType).toLowerCase()).search(request);
//
//        result.setKeyword(results.getQuery());
//        result.setData(JSON.parseArray(JSON.toJSONBytes(results.getHits())));
//        result.setTotal(results.getEstimatedTotalHits());
//        result.setEntityType(entityType);
//        result.setOffset(results.getOffset());
//        result.setLimit(results.getLimit());
//
//        return result;
//    }
//
//}
