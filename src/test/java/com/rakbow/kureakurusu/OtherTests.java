package com.rakbow.kureakurusu;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rakbow.kureakurusu.dao.AlbumMapper;
import com.rakbow.kureakurusu.dao.BookMapper;
import com.rakbow.kureakurusu.dao.EntityRelationMapper;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.emun.common.RelatedType;
import com.rakbow.kureakurusu.data.entity.Album;
import com.rakbow.kureakurusu.data.entity.Book;
import com.rakbow.kureakurusu.data.entity.EntityRelation;
import jakarta.annotation.Resource;
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
 * @since 2024/3/3 23:26
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = KureaKurusuApplication.class)
public class OtherTests {

    @Resource
    private AlbumMapper albumMapper;
    @Resource
    private BookMapper bookMapper;
    @Resource
    private EntityRelationMapper relationMapper;
    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Test
    public void batchAddRelation() {
        List<Album> albums = albumMapper.selectList(null);
        List<EntityRelation> addRelations = new ArrayList<>();

        int albumType = Entity.ALBUM.getValue();
        int proType = Entity.PRODUCT.getValue();


        for (Album album : albums) {
//            if(album.getProducts().isEmpty()) continue;

//            List<Long> productIds = album.getProducts();
            List<Long> productIds = new ArrayList<>();
            for (long proId : productIds) {
                long prodId = proId - 15;
                List<EntityRelation> tmpRelations = relationMapper.selectList(
                        new LambdaQueryWrapper<EntityRelation>()
                                .eq(EntityRelation::getEntityType, albumType)
                                .eq(EntityRelation::getEntityId, album.getId())
                                .eq(EntityRelation::getRelatedEntityType, proType)
                                .eq(EntityRelation::getRelatedEntityId, prodId)
                );
                if(!tmpRelations.isEmpty()) continue;

                EntityRelation relation = new EntityRelation();
                relation.setEntityType(albumType);
                relation.setEntityId(album.getId());
                relation.setRelatedEntityType(proType);
                relation.setRelatedEntityId(prodId);
                relation.setRelatedType(RelatedType.MAIN_ENTRY);
                addRelations.add(relation);
            }
        }
        MybatisBatch.Method<EntityRelation> method = new MybatisBatch.Method<>(EntityRelationMapper.class);
        MybatisBatch<EntityRelation> batchInsert = new MybatisBatch<>(sqlSessionFactory, addRelations);
        batchInsert.execute(method.insert());

    }

    @Test
    public void batchAddRelationBook() {
        List<Book> items = bookMapper.selectList(null);
        List<EntityRelation> addRelations = new ArrayList<>();

        int bookType = Entity.BOOK.getValue();
        int proType = Entity.PRODUCT.getValue();


        for (Book item : items) {
            if(item.getProducts().isEmpty()) continue;

            List<Long> productIds = item.getProducts();
//            List<Long> productIds = new ArrayList<>();
            for (long proId : productIds) {
                long prodId = proId - 14;
                List<EntityRelation> tmpRelations = relationMapper.selectList(
                        new LambdaQueryWrapper<EntityRelation>()
                                .eq(EntityRelation::getEntityType, bookType)
                                .eq(EntityRelation::getEntityId, item.getId())
                                .eq(EntityRelation::getRelatedEntityType, proType)
                                .eq(EntityRelation::getRelatedEntityId, prodId)
                );
                if(!tmpRelations.isEmpty()) continue;

                EntityRelation relation = new EntityRelation();
                relation.setEntityType(bookType);
                relation.setEntityId(item.getId());
                relation.setRelatedEntityType(proType);
                relation.setRelatedEntityId(prodId);
                relation.setRelatedType(RelatedType.MAIN_ENTRY);
                addRelations.add(relation);
            }
        }
        MybatisBatch.Method<EntityRelation> method = new MybatisBatch.Method<>(EntityRelationMapper.class);
        MybatisBatch<EntityRelation> batchInsert = new MybatisBatch<>(sqlSessionFactory, addRelations);
        batchInsert.execute(method.insert());

    }

}
