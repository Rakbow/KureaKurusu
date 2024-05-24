package com.rakbow.kureakurusu;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.rakbow.kureakurusu.dao.ImageMapper;
import com.rakbow.kureakurusu.dao.ItemMapper;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.emun.ImageType;
import com.rakbow.kureakurusu.data.entity.Item;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.data.image.TempImage;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2024/5/24 11:12
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = KureaKurusuApp.class)
public class DatabaseTests {

    @Resource
    private ImageMapper imageMapper;
    @Resource
    private ItemMapper itemMapper;
    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Test
    public void moveImage() {
        List<Item> items = itemMapper.selectList(null);
        List<Image> images = new ArrayList<>();
        for (Item item : items) {
            for (TempImage t : item.getImages()) {
                Image image = new Image();
                image.setEntityType(EntityType.ITEM);
                image.setEntityId(item.getId());
                if(t.getType() == 2)
                    image.setType(ImageType.OTHER);
                else if (t.getType() == 1)
                    image.setType(ImageType.MAIN);
                else
                    image.setType(ImageType.DEFAULT);
                image.setName(t.getNameEn());
                image.setNameZh(t.getNameZh());
                image.setUrl(t.getUrl());
                image.setDetail(t.getDescription());
                image.setAddedTime(DateHelper.stringToTimestamp(t.getUploadTime()));
                image.setEditedTime(image.getAddedTime());
                image.setStatus(true);
                images.add(image);
            }
        }
        MybatisBatch.Method<Image> method = new MybatisBatch.Method<>(ImageMapper.class);
        MybatisBatch<Image> batchInsert = new MybatisBatch<>(sqlSessionFactory, images);
        batchInsert.execute(method.insert());
    }

}
