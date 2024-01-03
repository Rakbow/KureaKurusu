package com.rakbow.kureakurusu.service;

import com.alibaba.fastjson2.JSON;
import com.rakbow.kureakurusu.data.vo.album.AlbumVOGamma;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import redis.clients.jedis.UnifiedJedis;
import redis.clients.jedis.search.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Rakbow
 * @since 2023-05-10 16:42
 */
@Service
public class SearchService {
    private final String INDEX_NAME = "album";

    private final Schema SCHEMA = new Schema()
            .addTextField("catalogNo", 1.0)  //专辑编号，权重为1
            .addTextField("name", 1.0)  //专辑名称（日语），权重为1
            .addTextField("nameEn", 0.5)  //专辑英文名称，权重为0.5
            .addTextField("nameZh", 0.5)  //专辑中文名称，权重为0.5
            .addTextField("franchises", 0.2)  //所属系列，权重为0.2
            .addTextField("albumFormat", 0.3)  //专辑分类，权重为0.3
            .addNumericField("id")  //表主键
            .addNumericField("hasBonus")  //是否包含特典内容
            .addTagField("products")  //所属产品id，在mysql中以JSON格式存储
            .addTextField("releaseDate", 0)  //发行日期，权重为0.4
            .addTextField("cover", 0)  //封面，权重为0.3
            .addNumericField("visitCount")  //浏览数
            .addNumericField("likeCount");  //点赞数

    @Resource
    private UnifiedJedis client;

    /**
     * 创建索引
     *
     * @param indexName 索引名称
     * @param prefix  要索引的数据前缀
     * @param schema  索引字段配置
     */
    public void createIndex(String indexName, String prefix, Schema schema) {
        IndexDefinition rule = new IndexDefinition(IndexDefinition.Type.HASH)
                .setPrefixes(prefix);
                // .setLanguage(Constants.GOODS_IDX_LANGUAGE); //设置支持中文分词
        client.ftCreate(indexName, IndexOptions.defaultOptions().setDefinition(rule), schema);
    }

    /**
     * 添加索引数据
     *
     * @param keyPrefix 要索引的数据前缀
     * @param album     商品信息
     * @return boolean
     */
    public boolean addAlbum(String keyPrefix, AlbumVOGamma album) {
        Map<String, Object> jsonObject = JSON.parseObject(JSON.toJSONString(album), HashMap.class);
        Map<String, String> doc = new HashMap<>();
        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            doc.put(key, String.valueOf(value));
        }
        // hash.put("_language", Constants.GOODS_IDX_LANGUAGE);
        client.hset(keyPrefix + album.getId(), doc);
        return true;
    }

    public SearchResult search(String indexName, String keyword) {
        // String queryKey = String.format("@goodsName:(%s)", keyword);
        Query q = new Query(keyword);
        String sort = "id";
        boolean order = false;
        // 查询是否排序
        if (StringUtils.isNotBlank(sort)) {
            q.setSortBy(sort, order);

        }
        // 设置中文分词查询
        // q.setLanguage(Constants.GOODS_IDX_LANGUAGE);
        // 查询分页
        // q.limit((int) page.offset(), page.getSize());
        // 返回查询结果
        return client.ftSearch(indexName, q);
    }

}
