package com.rakbow.kureakurusu;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rakbow.kureakurusu.dao.*;
import com.rakbow.kureakurusu.data.ItemTypeRelation;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.entity.entry.Entry;
import com.rakbow.kureakurusu.data.entity.item.Item;
import com.rakbow.kureakurusu.toolkit.*;
import jakarta.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = KureaKurusuApp.class)
public class RedisTests {

    @Resource
    private ItemMapper itemMapper;

    @Resource
    private StatisticMapper statisticMapper;
    @Resource
    private VisitUtil visitUtil;
    @Resource
    private LikeUtil likeUtil;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private PopularUtil popularUtil;

//    @Test
//    public void deleteAllCache() {
//        List<String> keys = redisUtil.keys("VISIT_*");
//        keys.forEach(key -> {
//            redisUtil.delete(key);
//        });
//    }

    @Test
    public void redisTest3() {


    }

    @Test
    public void refreshRelatedInfos() {
//        List<Album> albums = albumMapper.getAll();
//        albums.forEach(album -> albumService.generateRelatedAlbumIds(album));
        // List<String> keys = redisUtil.keys("entity_related_item:*");
        // keys.forEach(key -> redisUtil.delete(key));

        long t1 = DateHelper.now().getTime();


        long t2 = DateHelper.now().getTime();

        System.out.println(t2 - t1);

    }

    @Test
    public void generateItemTypeRelation() {
        redisUtil.delete("item_type_related:*");
        List<Item> items = itemMapper.selectList(null);
        for (Item item : items) {
            ItemTypeRelation relation = new ItemTypeRelation(item);
            String key = STR."item_type_related:\{item.getId()}";
            redisUtil.set(key, relation);
            System.out.println(STR."save to redis item id:\{item.getId()}");
        }
    }

//    @Test
//    public void resetRedis() {
//        List<Item> items = itemMapper.selectList(null);
//        String[] tmpSplitKey;
//        List<String> keys = redisUtil.keys("like:*");
//        for (String key : keys) {
//            tmpSplitKey = key.split(":");
//            int orgEntityType = Integer.parseInt(tmpSplitKey[1]);
//            long orgEntityId = Long.parseLong(tmpSplitKey[2]);
//        }
//
//    }

    @Test
    public void calculateEntityPopular() {
        int type = EntityType.ITEM.getValue();
        // Class<? extends Entry> subClass = EntryUtil.getSubClass(type);
        // BaseMapper<Entry> subMapper = MyBatisUtil.getMapper(subClass);
        // List<Entry> entries = subMapper.selectList(null);

        List<Item> items = itemMapper.selectList(null);

        for (Item e : items) {
            long visit = visitUtil.get(type, e.getId());
            // long like = likeUtil.get(type, e.getId());
            // double hotness = EntityPopularCalculator.calculateHotness(visit, like, e.getAddedTime().getTime());
            // if (hotness < 1) continue;
            // System.out.println(STR."ID: \{e.getId()}| HOT: \{hotness}| VISIT: \{visit}| LIKE: \{like}| NAME: \{e.getName()}");
            if(visit == 1) continue;
            popularUtil.updatePopularity(type, e.getId());
            System.out.println(STR."\{e.getId()} success");
        }
    }

}
