package com.rakbow.kureakurusu;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.rakbow.kureakurusu.dao.*;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.CommonConstant;
import com.rakbow.kureakurusu.data.ItemTypeRelation;
import com.rakbow.kureakurusu.data.RedisKey;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.emun.EntryType;
import com.rakbow.kureakurusu.data.emun.ImageType;
import com.rakbow.kureakurusu.data.entity.Entry;
import com.rakbow.kureakurusu.data.entity.Relation;
import com.rakbow.kureakurusu.data.entity.Role;
import com.rakbow.kureakurusu.data.entity.item.Item;
import com.rakbow.kureakurusu.data.entity.resource.Image;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.service.ResourceService;
import com.rakbow.kureakurusu.toolkit.*;
import com.rakbow.kureakurusu.toolkit.file.QiniuImageUtil;
import jakarta.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = KureaKurusuApp.class)
public class RedisTests {

    @Resource
    private ItemMapper itemMapper;
    @Resource
    private EntryMapper entryMapper;
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
    @Resource
    private HotnessCalculator hotnessCalculator;
    @Resource
    private QiniuImageUtil qiniuImageUtil;
    @Resource
    private ResourceService resourceSrv;
    @Resource
    private RoleMapper roleMapper;

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
    public void calculateItemPopular() {
        int type = EntityType.ITEM.getValue();
        List<Item> entities = itemMapper.selectList(null);
        int total = entities.size();
        int cur = 0;
        for (Item e : entities) {
            cur++;
            long visit = visitUtil.get(type, e.getId());
            if (visit == 1) continue;
            popularUtil.updateEntityPopularity(type, e.getId());
            System.out.println(STR."\{cur}/\{total} \{e.getId()} success");
        }
    }

    @Test
    public void calculateEntryPopular() {
        int entityType = EntityType.ENTRY.getValue();
        for (EntryType type : EntryType.values()) {
            List<Entry> entries = entryMapper.selectList(
                    new MPJLambdaWrapper<Entry>()
                            .selectAll(Entry.class)
                            .selectCount(Relation::getId, "items")
                            .leftJoin(Relation.class,
                                    on -> on.eq(Relation::getRelatedEntityId, Entry::getId)
                                            .eq(Relation::getEntityType, EntityType.ITEM.getValue())
                                            .eq(Relation::getRelatedEntityType, EntityType.ENTRY.getValue())
                            )
                            .groupBy(Entry::getId)
                            .eq(Entry::getType, type)
            );
            int total = entries.size();
            int cur = 0;
            String key = null;
            switch (type) {
                case EntryType.PRODUCT -> key = RedisKey.PRODUCT_POPULAR_RANK;
                case EntryType.PERSON -> key = RedisKey.PERSON_POPULAR_RANK;
                case EntryType.CHARACTER -> key = RedisKey.CHARACTER_POPULAR_RANK;
                case EntryType.CLASSIFICATION -> key = RedisKey.CLASSIFICATION_POPULAR_RANK;
                case EntryType.MATERIAL -> key = RedisKey.MATERIAL_POPULAR_RANK;
                case EntryType.EVENT -> key = RedisKey.EVENT_POPULAR_RANK;
            }
            redisUtil.delete(key);
            for (Entry e : entries) {
                cur++;
                if(e.getItems() == 0) continue;
                long visit = visitUtil.get(entityType, e.getId());
                long like = likeUtil.get(entityType, e.getId());
                double hotness = hotnessCalculator.calculateEntryHotness(visit, like, e.getItems());
                redisUtil.updateZSet(key, e.getId(), hotness);
                redisUtil.updateZSet(RedisKey.ENTRY_POPULAR_RANK, e.getId(), hotness);
                System.out.println(STR."\{type.getValue()} \{cur}/\{total} \{e.getId()} success");
            }
        }
    }

    @Test
    public void batchUpdateItemCoverAndThumbRedisCache() {
        List<Item> items = itemMapper.selectList(null);
        redisUtil.delete("entity_image_cache:*");
        String coverKey = STR."entity_image_cache:\{ImageType.MAIN.getValue()}:\{EntityType.ITEM.getValue()}:%s";
        String thumbKey = STR."entity_image_cache:\{ImageType.THUMB.getValue()}:\{EntityType.ITEM.getValue()}:%s";

        String curThumb;

        int total = items.size();
        AtomicInteger cur = new AtomicInteger();
        for (Item i : items) {
            Image cover = imageMapper.selectOne(new LambdaQueryWrapper<Image>()
                    .eq(Image::getEntityType, EntityType.ITEM.getValue())
                    .eq(Image::getEntityId, i.getId()).eq(Image::getType, ImageType.MAIN));
            Image thumb = imageMapper.selectOne(new LambdaQueryWrapper<Image>()
                    .eq(Image::getEntityType, EntityType.ITEM.getValue())
                    .eq(Image::getEntityId, i.getId()).eq(Image::getType, ImageType.THUMB));
            // if(thumb != null) {
            //     curThumb = thumb.getUrl();
            // }else if(cover != null) {
            //     String coverBase64Code = CommonImageUtil.getBase64CodeByUrl(cover.getUrl());
            //     String thumbBase64Code = CommonImageUtil.generateThumb(coverBase64Code);
            //     ImageMiniDTO thumbDto = new ImageMiniDTO();
            //     // thumbDto.setBase64Code(thumbBase64Code);
            //     thumbDto.setName("Thumb");
            //     thumbDto.setType(ImageType.THUMB.getValue());
            //     Image thumbImage = qiniuImageUtil.uploadImages(EntityType.ITEM.getValue(), i.getId(), List.of(thumbDto)).getFirst();
            //     imageMapper.insert(thumbImage);
            //     curThumb = thumbImage.getUrl();
            // }else {
            //     curThumb = CommonConstant.EMPTY_IMAGE_URL;
            // }

            redisUtil.set(String.format(coverKey, i.getId()), cover == null ? CommonConstant.EMPTY_IMAGE_URL : cover.getUrl());
            redisUtil.set(String.format(thumbKey, i.getId()), thumb == null ? CommonConstant.EMPTY_IMAGE_URL : thumb.getUrl());
            System.out.println(STR."\{cur.incrementAndGet()}/\{total} id: \{i.getId()} success");
        }
    }

    @Test
    public void batchUpdateEntryCoverAndThumbRedisCache() {
        // int type = EntityType.CHARACTER.getValue();
        // Class<? extends Entry> subClass = EntryUtil.getSubClass(type);
        // BaseMapper<Entry> subMapper = MyBatisUtil.getMapper(subClass);
        // List<Entry> entries = subMapper.selectList(null);
        // redisUtil.delete("entity_image_cache:*");
        // String coverKey = STR."entity_image_cache:\{ImageType.MAIN.getValue()}:\{type}:%s";
        // String thumbKey = STR."entity_image_cache:\{ImageType.THUMB.getValue()}:\{type}:%s";
        // int total = entries.size();
        // AtomicInteger cur = new AtomicInteger();
        // entries.forEach(i -> {
        //     Image cover = imageMapper.selectOne(new LambdaQueryWrapper<Image>()
        //             .eq(Image::getEntityType, type)
        //             .eq(Image::getEntityId, i.getId()).eq(Image::getType, ImageType.MAIN));
        //     Image thumb = imageMapper.selectOne(new LambdaQueryWrapper<Image>()
        //             .eq(Image::getEntityType, type)
        //             .eq(Image::getEntityId, i.getId()).eq(Image::getType, ImageType.THUMB));
        //     redisUtil.set(String.format(coverKey, i.getId()), cover == null ? CommonConstant.EMPTY_IMAGE_URL : cover.getUrl());
        //     redisUtil.set(String.format(thumbKey, i.getId()), thumb == null ? CommonConstant.EMPTY_IMAGE_URL : thumb.getUrl());
        //     System.out.println(STR."\{cur.incrementAndGet()}/\{total} id: \{i.getId()} success");
        // });
    }

    @Test
    public void batchGetEntryTableCountRedisCache() {
        // int type = EntityType.SUBJECT.getValue();
        // Class<? extends Entry> subClass = EntryUtil.getSubClass(type);
        // BaseMapper<Entry> subMapper = MyBatisUtil.getMapper(subClass);
        // List<Entry> entries = subMapper.selectList(null);
        // redisUtil.set(STR."entity_table_total:\{type}", entries.size());
    }

    @Test
    public void resetOptionRedisCache() {
        List<Role> roles = roleMapper.selectList(new LambdaQueryWrapper<Role>().orderByAsc(Role::getId));
        roles.forEach(i -> {
            MetaData.optionsZh.roleSet.add(new Attribute<>(i.getNameZh(), i.getId()));
            MetaData.optionsEn.roleSet.add(new Attribute<>(i.getNameEn(), i.getId()));
        });
        redisUtil.set(STR."\{RedisKey.OPTION_ROLE_SET}:zh", MetaData.optionsZh.roleSet);
    }

}
