package com.rakbow.kureakurusu;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.github.yulichang.wrapper.UpdateJoinWrapper;
import com.rakbow.kureakurusu.dao.*;
import com.rakbow.kureakurusu.data.dto.AlbumListParams;
import com.rakbow.kureakurusu.data.dto.AlbumUpdateDTO;
import com.rakbow.kureakurusu.data.emun.Entity;
import com.rakbow.kureakurusu.data.emun.RelatedType;
import com.rakbow.kureakurusu.data.entity.*;
import com.rakbow.kureakurusu.interceptor.AuthorityInterceptor;
import com.rakbow.kureakurusu.data.entity.Item;
import com.rakbow.kureakurusu.dao.ItemMapper;
import com.rakbow.kureakurusu.util.common.DataFinder;
import com.rakbow.kureakurusu.util.common.DataSorter;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
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
                if (!tmpRelations.isEmpty()) continue;

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
                if (!tmpRelations.isEmpty()) continue;

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
    public void updateItems() {
        List<Album> allAlbums = albumMapper.selectList(null);
        allAlbums.sort(DataSorter.albumIdSorter);
        List<Book> allBooks = bookMapper.selectList(null);
        allBooks.sort(DataSorter.bookIdSorter);
        List<Item> allItems = itemMapper.selectList(null);

        for (Item i : allItems) {
            if (i.getType() == Entity.ALBUM) {
                Album album = DataFinder.findAlbumById(i.getEntityId(), allAlbums);
                if(album == null) continue;
                i.setReleaseDate(album.getReleaseDate());
                i.setEan13(album.getBarcode());
            }
            if (i.getType() == Entity.BOOK) {
                Book book = DataFinder.findBookById(i.getEntityId(), allBooks);
                if(book == null) continue;
                i.setReleaseDate(book.getPublishDate());
                i.setEan13(book.getIsbn13());
            }
            UpdateWrapper<Item> wrapper = new UpdateWrapper<Item>()
                    .set("release_date", i.getReleaseDate())
                    .set("ean13", i.getEan13())
                    .eq("id", i.getId());
            itemMapper.update(wrapper);
        }

    }

//    @Test
//    public void joinListTest() {
//        MPJLambdaWrapper<Item> wrapper = new MPJLambdaWrapper<Item>()
//                .selectAll(Item.class)
//                .selectAll(ItemAlbum.class)
//                .leftJoin(ItemAlbum.class, ItemAlbum::getId, Item::getEntityId)
//                .eq(Item::getType, Entity.ALBUM)
//                .eq(Item::getEntityId, 11);
//        List<Album> list = itemMapper.selectJoinList(Album.class, wrapper);
//        System.out.println(list.getFirst());
//    }
//
//    @Test
//    public void joinPageTest(AlbumListParams param) {
//        MPJLambdaWrapper<Item> wrapper = new MPJLambdaWrapper<Item>()
//                .selectAll(Item.class)
//                .selectAll(ItemAlbum.class)
//                .leftJoin(ItemAlbum.class, ItemAlbum::getId, Item::getEntityId)
//                .like(Item::getName, param.getName())
//                .like(Item::getNameZh, param.getNameZh())
//                .like(Item::getNameEn, param.getNameEn())
//                .like(ItemAlbum::getCatalogNo, param.getCatalogNo())
//                .like(ItemAlbum::getBarcode, param.getBarcode())
//                .in(CollectionUtils.isNotEmpty(param.getAlbumFormat()), ItemAlbum::getAlbumFormat, param.getAlbumFormat())
//                .in(CollectionUtils.isNotEmpty(param.getPublishFormat()), ItemAlbum::getPublishFormat, param.getPublishFormat())
//                .in(CollectionUtils.isNotEmpty(param.getMediaFormat()), ItemAlbum::getMediaFormat, param.getMediaFormat())
//                .orderBy(param.isSort(), param.asc(), param.sortField);
//        IPage<Album> pages = itemMapper.selectJoinPage(new Page<>(param.getPage(), param.getSize()), Album.class, wrapper);
////        List<AlbumVOAlpha> items = VOMapper.toVOAlpha(pages.getRecords());
////        return new SearchResult<>(items, pages.getTotal(), pages.getCurrent(), pages.getSize());
//    }
//
//    @Test
//    public void joinOneTest() {
//        long id = 11;
//        MPJLambdaWrapper<Item> wrapper = new MPJLambdaWrapper<Item>()
//                .selectAll(Item.class)
//                .selectAll(ItemAlbum.class)
//                .leftJoin(ItemAlbum.class, ItemAlbum::getId, Item::getEntityId)
//                .eq(Item::getId, id);
//        Album album = itemMapper.selectJoinOne(Album.class, wrapper);
//    }
//
//    @Test
//    public void updateJoinTest(AlbumUpdateDTO dto) {
//        Item item = new Item(dto.getName());
//        item.setId(dto.getId());
//        item.setNameZh(dto.getNameZh());
//        item.setNameEn(dto.getNameEn());
//        item.setRemark(dto.getRemark());
//
//        ItemAlbum album = new ItemAlbum(dto);
//        UpdateJoinWrapper<Item> update = JoinWrappers.update(Item.class)
//                //设置两个副表的 set 语句
//                .setUpdateEntity(album)
//                //address和area 两张表空字段和非空字段一起更新 可以改成如下setUpdateEntityAndNull
//                //.setUpdateEntityAndNull(address, area)
//                .leftJoin(ItemAlbum.class, ItemAlbum::getId, Item::getEntityId)
//                .eq(Item::getId, album.getId());
//        itemMapper.updateJoin(item, update);
//    }

}
