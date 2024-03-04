package com.rakbow.kureakurusu;

import com.rakbow.kureakurusu.dao.*;
import com.rakbow.kureakurusu.data.RedisKey;
import com.rakbow.kureakurusu.data.entity.EntityStatistic;
import com.rakbow.kureakurusu.service.*;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.common.LikeUtil;
import com.rakbow.kureakurusu.util.common.RedisUtil;
import com.rakbow.kureakurusu.util.common.VisitUtil;
import jakarta.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = KureaKurusuApplication.class)
public class RedisTests {

    @Resource
    private UserService userService;
    @Resource
    private EntityService entityService;
    @Resource
    private AlbumService albumService;
    @Resource
    private FranchiseService franchiseService;
    @Resource
    private ProductService productService;
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
    private ProductMapper productMapper;
    @Resource
    private StatisticMapper statisticMapper;
    @Resource
    private FranchiseMapper franchiseMapper;
    @Resource
    private VisitUtil visitUtil;
    @Resource
    private LikeUtil likeUtil;
    @Resource
    private RedisUtil redisUtil;

    @Test
    public void redisTest1() {
        redisUtil.redisTemplate.opsForZSet().add(RedisKey.ALBUM_VISIT_RANKING, 1, 0);
    }

    @Test
    public void deleteAllCache() {
        List<String> keys = redisUtil.keys("VISIT_*");
        keys.forEach(key -> {
            redisUtil.delete(key);
        });
    }

    @Test
    public void redisTest3() {

        List<String> keys = redisUtil.keys("visit:");
        keys.addAll(redisUtil.keys("like:"));
        keys.forEach(key-> {
            redisUtil.delete(key);
        });

        List<EntityStatistic> statistics = statisticMapper.getAll();

        statistics.forEach(s-> {
            redisUtil.set(visitUtil.getSingleVisitKey(s.getEntityType(), s.getEntityId()), (int)s.getVisitCount());
            redisUtil.set(likeUtil.getEntityLikeKey(s.getEntityType(), s.getEntityId()), (int)s.getLikeCount());
        });

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

}
