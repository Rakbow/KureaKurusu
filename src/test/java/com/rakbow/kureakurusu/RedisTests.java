package com.rakbow.kureakurusu;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rakbow.kureakurusu.dao.*;
import com.rakbow.kureakurusu.data.CommonConstant;
import com.rakbow.kureakurusu.data.ItemTypeRelation;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.emun.ImageType;
import com.rakbow.kureakurusu.data.emun.ItemType;
import com.rakbow.kureakurusu.data.entity.item.Item;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.toolkit.*;
import jakarta.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = KureaKurusuApp.class)
public class RedisTests {

    @Resource
    private ItemMapper itemMapper;
    @Resource
    private ImageMapper imageMapper;
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
            if (visit == 1) continue;
            popularUtil.updatePopularity(type, e.getId());
            System.out.println(STR."\{e.getId()} success");
        }
    }

    @Test
    public void batchUpdateItemCoverAndThumbRedisCache() {
        List<Item> items = itemMapper.selectList(new LambdaQueryWrapper<Item>().eq(Item::getType, ItemType.ALBUM));
        redisUtil.delete("entity_image_cache:*");
        String coverKey = STR."entity_image_cache:\{ImageType.MAIN.getValue()}:\{EntityType.ITEM.getValue()}:%s";
        String thumbKey = STR."entity_image_cache:\{ImageType.THUMB.getValue()}:\{EntityType.ITEM.getValue()}:%s";
        int total = items.size();
        AtomicInteger cur = new AtomicInteger();
        items.forEach(i -> {
            Image cover = imageMapper.selectOne(new LambdaQueryWrapper<Image>()
                    .eq(Image::getEntityType, EntityType.ITEM.getValue())
                    .eq(Image::getEntityId, i.getId()).eq(Image::getType, ImageType.MAIN));
            Image thumb = imageMapper.selectOne(new LambdaQueryWrapper<Image>()
                    .eq(Image::getEntityType, EntityType.ITEM.getValue())
                    .eq(Image::getEntityId, i.getId()).eq(Image::getType, ImageType.THUMB));
            redisUtil.set(String.format(coverKey, i.getId()), cover == null ? CommonConstant.EMPTY_IMAGE_URL : cover.getUrl());
            redisUtil.set(String.format(thumbKey, i.getId()), thumb == null ? CommonConstant.EMPTY_IMAGE_URL : thumb.getUrl());
            System.out.println(STR."\{cur.incrementAndGet()}/\{total} id: \{i.getId()} success");
        });
    }

}
