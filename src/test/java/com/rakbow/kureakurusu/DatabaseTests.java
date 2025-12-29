package com.rakbow.kureakurusu;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Rakbow
 * @since 2024/5/24 11:12
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = KureaKurusuApp.class)
public class DatabaseTests {

//    @Test
//    public void moveImage() {
//        List<Item> items = itemMapper.selectList(null);
//        List<Image> images = new ArrayList<>();
//        for (Item item : items) {
//            for (TempImage t : item.getImages()) {
//                Image image = new Image();
//                image.setEntityType(EntityType.ITEM);
//                image.setEntityId(item.getId());
//                if(t.getType() == 2)
//                    image.setType(ImageType.OTHER);
//                else if (t.getType() == 1)
//                    image.setType(ImageType.MAIN);
//                else
//                    image.setType(ImageType.DEFAULT);
//                image.setName(t.getNameEn());
//                image.setNameZh(t.getNameZh());
//                image.setUrl(t.getUrl());
//                image.setDetail(t.getDescription());
//                image.setAddedTime(DateHelper.stringToTimestamp(t.getUploadTime()));
//                image.setEditedTime(image.getAddedTime());
//                image.setStatus(true);
//                images.add(image);
//            }
//        }
//        MybatisBatch.Method<Image> method = new MybatisBatch.Method<>(ImageMapper.class);
//        MybatisBatch<Image> batchInsert = new MybatisBatch<>(sqlSessionFactory, images);
//        batchInsert.execute(method.insert());
//    }

}
