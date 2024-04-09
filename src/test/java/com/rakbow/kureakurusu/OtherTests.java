package com.rakbow.kureakurusu;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.rakbow.kureakurusu.dao.*;
import com.rakbow.kureakurusu.data.emun.Entity;
import com.rakbow.kureakurusu.data.emun.RelatedType;
import com.rakbow.kureakurusu.data.entity.*;
import com.rakbow.kureakurusu.interceptor.AuthorityInterceptor;
import com.rakbow.kureakurusu.util.common.DataSorter;
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
@ContextConfiguration(classes = KureaKurusuApp.class)
public class OtherTests {

    @Resource
    private AlbumMapper albumMapper;
    @Resource
    private BookMapper bookMapper;
    @Resource
    private EntityRelationMapper relationMapper;
    @Resource
    private PersonRelationMapper personRelationMapper;
    @Resource
    private SqlSessionFactory sqlSessionFactory;
    @Resource
    private UserMapper userMapper;
    @Resource
    private ItemMapper itemMapper;

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
//            if(item.getProducts().isEmpty()) continue;

//            List<Long> productIds = item.getProducts();
            List<Long> productIds = new ArrayList<>();
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

    @Test
    public void batchAddPersonRelationBook() {
        AuthorityInterceptor.setCurrentUser(userMapper.selectById(1));
        List<Book> items = bookMapper.selectList(new LambdaQueryWrapper<Book>().ge(Book::getId, 108).le(Book::getId, 109));
        List<PersonRelation> addRelations = new ArrayList<>();

        int bookType = Entity.BOOK.getValue();

        long role1Id = 10;
        long person1Id = 2;
        long role2Id = 67;
        long person2Id = 11809;
        for (Book item : items) {
            PersonRelation r_1 = new PersonRelation();
            r_1.setPersonId(person1Id);
            r_1.setRoleId(role1Id);
            r_1.setEntityType(bookType);
            r_1.setEntityId(item.getId());
            addRelations.add(r_1);
            PersonRelation r_2 = new PersonRelation();
            r_2.setPersonId(person2Id);
            r_2.setRoleId(role2Id);
            r_2.setEntityType(bookType);
            r_2.setEntityId(item.getId());
            r_2.setMain(1);
            addRelations.add(r_2);
        }
        MybatisBatch.Method<PersonRelation> method = new MybatisBatch.Method<>(PersonRelationMapper.class);
        MybatisBatch<PersonRelation> batchInsert = new MybatisBatch<>(sqlSessionFactory, addRelations);
        batchInsert.execute(method.insert());
    }

    @Test
    public void mybatisPlusReflectTest() {
        String key = "birthDate";
        TableInfo tableInfo = TableInfoHelper.getTableInfo(Person.class);
        if (tableInfo != null) {
            TableFieldInfo fieldInfo = tableInfo.getFieldList().stream()
                    .filter(f -> f.getProperty().equals(key)) // 根据实体类属性名查找对应的字段信息
                    .findFirst()
                    .orElse(null);
            if (fieldInfo != null) {
                String columnName = fieldInfo.getColumn();
                System.out.println("实体类属性名: " + key + " 对应的表字段名为: " + columnName);
            }
        }
    }

    @Test
    public void addItems() {
        List<Album> allAlbums = albumMapper.selectList(null);
        List<Book> allBooks = bookMapper.selectList(null);
        List<Item> items = new ArrayList<>();
        for (Album album : allAlbums) {
            Item item = new Item();
            item.setName(album.getName());
            item.setNameZh(album.getNameZh());
            item.setNameEn(album.getNameEn());
            item.setType(Entity.ALBUM);
            item.setEntityId(album.getId());
            item.setImages(album.getImages());
            item.setDetail(album.getDetail());
            item.setRemark(album.getRemark());
            item.setAddedTime(album.getAddedTime());
            item.setEditedTime(album.getEditedTime());
            items.add(item);
        }
        for (Book book : allBooks) {
            Item item = new Item();
            item.setName(book.getTitle());
            item.setNameZh(book.getTitleZh());
            item.setNameEn(book.getTitleEn());
            item.setType(Entity.BOOK);
            item.setEntityId(book.getId());
            item.setImages(book.getImages());
            item.setDetail(book.getDetail());
            item.setRemark(book.getRemark());
            item.setAddedTime(book.getAddedTime());
            item.setEditedTime(book.getEditedTime());
            items.add(item);
        }
        items.sort(DataSorter.itemAddedTimeSorter);

        MybatisBatch.Method<Item> method = new MybatisBatch.Method<>(ItemMapper.class);
        MybatisBatch<Item> batchInsert = new MybatisBatch<>(sqlSessionFactory, items);
        batchInsert.execute(method.insert());
    }

    @Test
    public void joinTest() {
        MPJLambdaWrapper<Item> wrapper = new MPJLambdaWrapper<Item>()
                .selectAll(Item.class)
                .selectAll(ItemAlbum.class)
                .leftJoin(ItemAlbum.class, ItemAlbum::getId, Item::getEntityId)
                .eq(Item::getType, Entity.ALBUM)
                .eq(Item::getEntityId, 11);
        List<Album> list = itemMapper.selectJoinList(Album.class, wrapper);
        System.out.println(list.getFirst());
    }

}
