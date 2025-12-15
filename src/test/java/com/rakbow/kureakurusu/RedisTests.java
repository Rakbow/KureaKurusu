package com.rakbow.kureakurusu;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.rakbow.kureakurusu.dao.*;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.RedisKey;
import com.rakbow.kureakurusu.data.enums.EntityType;
import com.rakbow.kureakurusu.data.enums.EntryType;
import com.rakbow.kureakurusu.data.entity.Entry;
import com.rakbow.kureakurusu.data.entity.Relation;
import com.rakbow.kureakurusu.data.entity.Role;
import com.rakbow.kureakurusu.data.entity.item.Item;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.service.ImageService;
import com.rakbow.kureakurusu.toolkit.*;
import com.rakbow.kureakurusu.toolkit.file.QiniuImageUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
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
    private ImageService resourceSrv;
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

    // @Test
    // @SneakyThrows
    // public void batchUpdateItemCoverAndThumbRedisCache() {
    //     List<Item> items = itemMapper.selectList(new LambdaQueryWrapper<Item>().eq(Item::getType, ItemType.DISC.getValue()));
    //     redisUtil.delete("entity_image_cache:*");
    //     String coverKey = STR."entity_image_cache:\{ImageType.MAIN.getValue()}:\{EntityType.ITEM.getValue()}:%s";
    //     String thumbKey = STR."entity_image_cache:\{ImageType.THUMB.getValue()}:\{EntityType.ITEM.getValue()}:%s";
    //
    //     String curThumb;
    //
    //     int total = items.size();
    //     AtomicInteger cur = new AtomicInteger();
    //     for (Item i : items) {
    //         Image cover = imageMapper.selectOne(new LambdaQueryWrapper<Image>()
    //                 .eq(Image::getEntityType, EntityType.ITEM.getValue())
    //                 .eq(Image::getEntityId, i.getId()).eq(Image::getType, ImageType.MAIN));
    //         Image thumb = imageMapper.selectOne(new LambdaQueryWrapper<Image>()
    //                 .eq(Image::getEntityType, EntityType.ITEM.getValue())
    //                 .eq(Image::getEntityId, i.getId()).eq(Image::getType, ImageType.THUMB));
    //
    //         if(cover == null || thumb == null) continue;
    //         // 读取缩略图像
    //         BufferedImage image = ImageIO.read(new URL(thumb.getUrl()));
    //         int width = image.getWidth();
    //         int height = image.getHeight();
    //         //若不为长方形，删除原图，重新生成缩略图
    //         if(width == height) continue;
    //
    //         //删除原图(数据库，七牛云，redis缓存)
    //         resourceSrv.delete(List.of(new ImageDeleteMiniDTO(thumb.getId(), thumb.getUrl())));
    //         redisUtil.delete(String.format(thumbKey, i.getId()));
    //
    //         //生成新thumb
    //         ImageMiniDTO newThumb = new ImageMiniDTO();
    //         newThumb.setName("Thumb");
    //         newThumb.setType(ImageType.THUMB.getValue());
    //
    //         // 找最短边
    //         int side = Math.min(width, height);
    //
    //         // 计算起点（中心为基准）
    //         int x = (width - side) / 2;
    //         int y = (height - side) / 2;
    //
    //         // 裁剪为正方形
    //         BufferedImage cropped = image.getSubimage(x, y, side, side);
    //         // 生成缩略图到内存
    //         ByteArrayOutputStream os = new ByteArrayOutputStream();
    //         Thumbnails.of(cropped)
    //                 .size(70, 70)
    //                 .outputFormat("jpg")
    //                 .toOutputStream(os);
    //
    //         byte[] thumbBytes = os.toByteArray();
    //         // 创建 MultipartFile 对象（MockMultipartFile 实现类）
    //         MultipartFile thumbFile = new MockMultipartFile(
    //                 "file",// 字段名
    //                 "Thumb.jpg",// 文件名
    //                 "image/jpeg",// MIME 类型
    //                 thumbBytes// 内容
    //         );
    //
    //         // 设置生成的 MultipartFile 到 DTO
    //         newThumb.setFile(thumbFile);
    //
    //         //upload to qiniu
    //         List<Image> addImages = qiniuImageUtil.uploadImages(EntityType.ITEM.getValue(), i.getId(), List.of(newThumb));
    //         Image newThumbImage = addImages.getFirst();
    //         imageMapper.insert(newThumbImage);
    //
    //         redisUtil.set(String.format(thumbKey, i.getId()), newThumbImage.getUrl());
    //
    //         System.out.println(STR."\{cur.incrementAndGet()}/\{total} id: \{i.getId()} success");
    //     }
    // }

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
        redisUtil.delete(STR."\{RedisKey.OPTION_ROLE_SET}:zh");
        redisUtil.set(STR."\{RedisKey.OPTION_ROLE_SET}:zh", MetaData.optionsZh.roleSet);
        redisUtil.delete(STR."\{RedisKey.OPTION_ROLE_SET}:en");
        redisUtil.set(STR."\{RedisKey.OPTION_ROLE_SET}:en", MetaData.optionsEn.roleSet);
    }

}
