package com.rakbow.kureakurusu;

import com.rakbow.kureakurusu.dao.MusicMapper;
import com.rakbow.kureakurusu.service.AlbumService;
import com.rakbow.kureakurusu.service.MusicService;
import com.rakbow.kureakurusu.util.file.QiniuImageUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author Rakbow
 * @since 2022-12-01 23:56
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = KureaKurusuApplication.class)
public class otherTests {

    @Resource
    private AlbumService albumService;
    @Resource
    private MusicService musicService;
    @Resource
    private MusicMapper musicMapper;


    @Test
    public void tmpTest() {
        String url = "https://img.rakbow.com/album/6/f57fa8a51b594e30.jpg";
        System.out.println(QiniuImageUtil.getImageKeyByFullUrl(url));
    }

    // @Test
    // public void test1() {
    //     List<Music> musicList = musicMapper.selectList(new LambdaQueryWrapper<Music>().eq(Music::getStatus, 1).orderByAsc(Music::getAlbumId));
    //     List<Long> albumIds = musicList.stream().map(Music::getAlbumId).distinct().toList();
    //     List<Episode> addData = new ArrayList<>();
    //
    //     Comparator<Music> comparator = Comparator
    //             .comparing(Music::getDiscSerial)
    //             .thenComparing(Music::getTrackSerial);
    //
    //     for (long id : albumIds) {
    //         List<Music> tmpMusicList = musicList.stream().filter(music -> music.getAlbumId() == id).toList();
    //
    //         for (Music m : tmpMusicList) {
    //             Episode ep = new Episode();
    //
    //             ep.setTitle(m.getName());
    //             ep.setTitleEn(m.getNameEn());
    //             ep.setDuration(getDuration(m.getAudioLength()));
    //             ep.setDiscNum(m.getDiscSerial());
    //             ep.setSerial(Integer.parseInt(m.getTrackSerial()));
    //             ep.setRelatedId(id);
    //
    //             ep.setAddedTime(m.getAddedTime());
    //             ep.setEditedTime(m.getEditedTime());
    //
    //             addData.add(ep);
    //         }
    //     }
    //
    //     SqlSessionFactory sqlSessionFactory = SpringUtil.getBean("sqlSessionFactory");
    //     //批量新增
    //     MybatisBatch.Method<Episode> method = new MybatisBatch.Method<>(EpisodeMapper.class);
    //     MybatisBatch<Episode> batchInsert = new MybatisBatch<>(sqlSessionFactory, addData);
    //     batchInsert.execute(method.insert());
    //
    // }

    public static int getDuration(String time) {
        String[] parts = time.split(":");
        if (parts.length == 2) {
            try {
                int minutes = Integer.parseInt(parts[0]);
                int seconds = Integer.parseInt(parts[1]);

                return minutes * 60 + seconds;
            } catch (NumberFormatException e) {
                // 处理格式错误的情况
                System.err.println("Invalid time format");
            }
        } else {
            // 处理格式错误的情况
            System.err.println("Invalid time format");
        }

        return 0; // 或者返回合适的默认值
    }

    @Test
    public void test2() {

    }

}
