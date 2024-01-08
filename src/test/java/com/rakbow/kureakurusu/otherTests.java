package com.rakbow.kureakurusu;

import com.rakbow.kureakurusu.dao.MusicMapper;
import com.rakbow.kureakurusu.service.AlbumService;
import com.rakbow.kureakurusu.service.MusicService;
import com.rakbow.kureakurusu.util.entity.MusicUtil;
import com.rakbow.kureakurusu.util.file.QiniuImageUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.chrono.JapaneseDate;

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

    @Test
    public void test1() {
        System.out.println(JapaneseDate.now());
    }

}
