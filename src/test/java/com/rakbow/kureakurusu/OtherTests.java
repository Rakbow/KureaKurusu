package com.rakbow.kureakurusu;

import com.rakbow.kureakurusu.dao.EpisodeMapper;
import com.rakbow.kureakurusu.dao.ItemMapper;
import com.rakbow.kureakurusu.dao.RelationMapper;
import com.rakbow.kureakurusu.dao.UserMapper;
import jakarta.annotation.Resource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Currency;
import java.util.Locale;

/**
 * @author Rakbow
 * @since 2024/3/3 23:26
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = KureaKurusuApp.class)
public class OtherTests {

    @Resource
    private RelationMapper relationMapper;
    @Resource
    private SqlSessionFactory sqlSessionFactory;
    @Resource
    private UserMapper userMapper;
    @Resource
    private ItemMapper itemMapper;
    @Resource
    private EpisodeMapper epMapper;

    @Test
    public void javaTest() {
    }


//    @Test
//    public void batchAddRelation() {
//        List<Album> albums = albumMapper.selectList(null);
//        List<EntityRelation> addRelations = new ArrayList<>();
//
//        int albumType = Entity.ALBUM.getValue();
//        int proType = Entity.PRODUCT.getValue();
//
//
//        for (Album album : albums) {
////            if(album.getProducts().isEmpty()) continue;
//
////            List<Long> productIds = album.getProducts();
//            List<Long> productIds = new ArrayList<>();
//            for (long proId : productIds) {
//                long prodId = proId - 15;
//                List<EntityRelation> tmpRelations = relationMapper.selectList(
//                        new LambdaQueryWrapper<EntityRelation>()
//                                .eq(EntityRelation::getEntityType, albumType)
//                                .eq(EntityRelation::getEntityId, album.getId())
//                                .eq(EntityRelation::getRelatedEntityType, proType)
//                                .eq(EntityRelation::getRelatedEntityId, prodId)
//                );
//                if (!tmpRelations.isEmpty()) continue;
//
//                EntityRelation relation = new EntityRelation();
//                relation.setEntityType(albumType);
//                relation.setEntityId(album.getId());
//                relation.setRelatedEntityType(proType);
//                relation.setRelatedEntityId(prodId);
//                relation.setRelatedType(RelatedType.MAIN_ENTRY);
//                addRelations.add(relation);
//            }
//        }
//        MybatisBatch.Method<EntityRelation> method = new MybatisBatch.Method<>(EntityRelationMapper.class);
//        MybatisBatch<EntityRelation> batchInsert = new MybatisBatch<>(sqlSessionFactory, addRelations);
//        batchInsert.execute(method.insert());
//
//    }

//    @Test
//    public void batchAddRelationBook() {
//        List<Book> items = bookMapper.selectList(null);
//        List<EntityRelation> addRelations = new ArrayList<>();
//
//        int bookType = Entity.BOOK.getValue();
//        int proType = Entity.PRODUCT.getValue();
//
//
//        for (Book item : items) {
////            if(item.getProducts().isEmpty()) continue;
//
////            List<Long> productIds = item.getProducts();
//            List<Long> productIds = new ArrayList<>();
////            List<Long> productIds = new ArrayList<>();
//            for (long proId : productIds) {
//                long prodId = proId - 14;
//                List<EntityRelation> tmpRelations = relationMapper.selectList(
//                        new LambdaQueryWrapper<EntityRelation>()
//                                .eq(EntityRelation::getEntityType, bookType)
//                                .eq(EntityRelation::getEntityId, item.getId())
//                                .eq(EntityRelation::getRelatedEntityType, proType)
//                                .eq(EntityRelation::getRelatedEntityId, prodId)
//                );
//                if (!tmpRelations.isEmpty()) continue;
//
//                EntityRelation relation = new EntityRelation();
//                relation.setEntityType(bookType);
//                relation.setEntityId(item.getId());
//                relation.setRelatedEntityType(proType);
//                relation.setRelatedEntityId(prodId);
//                relation.setRelatedType(RelatedType.MAIN_ENTRY);
//                addRelations.add(relation);
//            }
//        }
//        MybatisBatch.Method<EntityRelation> method = new MybatisBatch.Method<>(EntityRelationMapper.class);
//        MybatisBatch<EntityRelation> batchInsert = new MybatisBatch<>(sqlSessionFactory, addRelations);
//        batchInsert.execute(method.insert());
//
//    }
//
//    @Test
//    public void batchAddPersonRelationBook() {
//        AuthorityInterceptor.setCurrentUser(userMapper.selectById(1));
//        List<Book> items = bookMapper.selectList(new LambdaQueryWrapper<Book>().ge(Book::getId, 108).le(Book::getId, 109));
//        List<PersonRelation> addRelations = new ArrayList<>();
//
//        int bookType = Entity.BOOK.getValue();
//
//        long role1Id = 10;
//        long person1Id = 2;
//        long role2Id = 67;
//        long person2Id = 11809;
//        for (Book item : items) {
//            PersonRelation r_1 = new PersonRelation();
//            r_1.setPersonId(person1Id);
//            r_1.setRoleId(role1Id);
//            r_1.setEntityType(bookType);
//            r_1.setEntityId(item.getId());
//            addRelations.add(r_1);
//            PersonRelation r_2 = new PersonRelation();
//            r_2.setPersonId(person2Id);
//            r_2.setRoleId(role2Id);
//            r_2.setEntityType(bookType);
//            r_2.setEntityId(item.getId());
//            r_2.setMain(1);
//            addRelations.add(r_2);
//        }
//        MybatisBatch.Method<PersonRelation> method = new MybatisBatch.Method<>(PersonRelationMapper.class);
//        MybatisBatch<PersonRelation> batchInsert = new MybatisBatch<>(sqlSessionFactory, addRelations);
//        batchInsert.execute(method.insert());
//    }
//
//    @Test
//    public void mybatisPlusReflectTest() {
//        String key = "birthDate";
//        TableInfo tableInfo = TableInfoHelper.getTableInfo(Person.class);
//        if (tableInfo != null) {
//            TableFieldInfo fieldInfo = tableInfo.getFieldList().stream()
//                    .filter(f -> f.getProperty().equals(key)) // 根据实体类属性名查找对应的字段信息
//                    .findFirst()
//                    .orElse(null);
//            if (fieldInfo != null) {
//                String columnName = fieldInfo.getColumn();
//                System.out.println("实体类属性名: " + key + " 对应的表字段名为: " + columnName);
//            }
//        }
//    }

//    @Test
//    public void addItems() {
//        List<Album> allAlbums = albumMapper.selectList(null);
//        List<Book> allBooks = bookMapper.selectList(null);
//
//        List<Item> items = new ArrayList<>();
//        for (Album album : allAlbums) {
//            Item item = new Item();
//
//            item.setId(0L);
//            item.setType(ItemType.ALBUM);
//            item.setOrgId(album.getId());
//            item.setName(album.getName());
//            item.setNameZh(album.getNameZh());
//            item.setNameEn(album.getNameEn());
//
//            item.setEan13(album.getEan13());
//            item.setReleaseDate(album.getReleaseDate());
//            item.setPrice(album.getPrice());
//            item.setCurrency(album.getCurrency());
//
//            item.setImages(album.getImages());
//            item.setDetail(album.getDetail());
//            item.setRemark(album.getRemark());
//
//            item.setAddedTime(album.getAddedTime());
//            item.setEditedTime(album.getEditedTime());
//            items.add(item);
//        }
//        for (Book book : allBooks) {
//            Item item = new Item();
//
//            item.setId(0L);
//            item.setType(ItemType.BOOK);
//            item.setOrgId(book.getId());
//            item.setName(book.getName());
//            item.setNameZh(book.getNameZh());
//            item.setNameEn(book.getNameEn());
//
//            item.setEan13(book.getEan13());
//            item.setReleaseDate(book.getReleaseDate());
//            item.setPrice(book.getPrice());
//            item.setCurrency(book.getCurrency());
//
//            item.setImages(book.getImages());
//            item.setDetail(book.getDetail());
//            item.setRemark(book.getRemark());
//
//            item.setAddedTime(book.getAddedTime());
//            item.setEditedTime(book.getEditedTime());
//            items.add(item);
//        }
//        items.sort(DataSorter.itemAddedTimeSorter);
//
//        MybatisBatch.Method<Item> method = new MybatisBatch.Method<>(ItemMapper.class);
//        MybatisBatch<Item> batchInsert = new MybatisBatch<>(sqlSessionFactory, items);
//        batchInsert.execute(method.insert());
//    }

//    @Test
//    public void addSubItems() {
//        List<Album> allAlbums = albumMapper.selectList(null);
//        List<Book> allBooks = bookMapper.selectList(null);
//        List<Item> allItems = itemMapper.selectList(null);
//
//        List<ItemAlbum> itemAlbums = new ArrayList<>();
//        List<ItemBook> itemBooks = new ArrayList<>();
//        for (Item item : allItems) {
//            if(item.getType() == ItemType.ALBUM) {
//                ItemAlbum itemAlbum = new ItemAlbum();
//
//                Album album = DataFinder.findAlbumById(item.getOrgId(), allAlbums);
//                if(album == null) continue;
//                itemAlbum.setId(item.getId());
//                itemAlbum.setCatalogNo(album.getCatalogNo());
//                itemAlbum.setPublishFormat(album.getPublishFormat());
//                itemAlbum.setAlbumFormat(album.getAlbumFormat());
//                itemAlbum.setMediaFormat(album.getMediaFormat());
//                itemAlbum.setHasBonus(album.getHasBonus());
//                itemAlbum.setBonus(album.getBonus());
//                itemAlbums.add(itemAlbum);
//            }else if(item.getType() == ItemType.BOOK) {
//                ItemBook itemBook = new ItemBook();
//
//                Book book = DataFinder.findBookById(item.getOrgId(), allBooks);
//                if(book == null) continue;
//                itemBook.setId(item.getId());
//                itemBook.setIsbn10(book.getIsbn10());
//                itemBook.setBookType(book.getBookType());
//                itemBook.setAuthors(book.getAuthors());
//                itemBook.setRegion(book.getRegion());
//                itemBook.setLang(book.getLang());
//                itemBook.setSummary(book.getSummary());
//                itemBook.setSpec(book.getSpec());
//                itemBook.setHasBonus(book.getHasBonus());
//                itemBook.setBonus(book.getBonus());
//                itemBooks.add(itemBook);
//            }
//        }
//        MybatisBatch.Method<ItemAlbum> albumMethod = new MybatisBatch.Method<>(ItemAlbumMapper.class);
//        MybatisBatch<ItemAlbum> albumBatchInsert = new MybatisBatch<>(sqlSessionFactory, itemAlbums);
//        albumBatchInsert.execute(albumMethod.insert());
//
//        MybatisBatch.Method<ItemBook> bookMethod = new MybatisBatch.Method<>(ItemBookMapper.class);
//        MybatisBatch<ItemBook> bookBatchInsert = new MybatisBatch<>(sqlSessionFactory, itemBooks);
//        bookBatchInsert.execute(bookMethod.insert());
//    }

//    @Test
//    public void updateItems() {
//        List<Album> allAlbums = albumMapper.selectList(null);
//        allAlbums.sort(DataSorter.albumIdSorter);
//        List<Book> allBooks = bookMapper.selectList(null);
//        allBooks.sort(DataSorter.bookIdSorter);
//        List<Item> allItems = itemMapper.selectList(null);
//
//        for (Item i : allItems) {
//            if (i.getType() == ItemType.ALBUM) {
//                Album album = DataFinder.findAlbumById(i.getEntityId(), allAlbums);
//                if(album == null) continue;
//                i.setPrice(album.getPrice());
//                i.setCurrency(album.getCurrency());
//            }
//            if (i.getType() == ItemType.BOOK) {
//                Book book = DataFinder.findBookById(i.getEntityId(), allBooks);
//                if(book == null) continue;
//                i.setPrice(book.getPrice());
//                i.setCurrency(book.getCurrency());
//            }
//            UpdateWrapper<Item> wrapper = new UpdateWrapper<Item>()
//                    .set("price", i.getPrice())
//                    .set("currency", i.getCurrency())
//                    .eq("id", i.getId());
//            itemMapper.update(wrapper);
//        }
//
//    }

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

//    @Test
//    public void updateEP() {
//        List<Item> items = itemMapper.selectList(null);
//        List<Episode> eps = epMapper.selectList(null);
//
//        for (Episode ep : eps) {
//            Optional<Item> result = items.stream()
//                    .filter(a -> a.getType() == ItemType.ALBUM && a.getOrgId() == ep.getRelatedId())
//                    .findFirst();
//            ep.setRelatedId(result.get().getId());
//
//            epMapper.update(
//                    ep,
//                    new LambdaUpdateWrapper<Episode>()
//                            .set(Episode::getRelatedId, ep.getRelatedId())
//                            .eq(Episode::getId, ep.getId())
//            );
//            System.out.println(STR."update success ep id: \{ep.getId()}, old related_id: \{result.get().getOrgId()}, new related_id: \{result.get().getId()}");
//        }
//    }
//
//    @Test
//    public void updatePersonnel() {
//        List<Item> items = itemMapper.selectList(null);
//        List<PersonRelation> prs = personRelationMapper.selectList(null);
//
//        for (PersonRelation pr : prs) {
//            Optional<Item> result = items.stream()
//                    .filter(a -> a.getType().getValue() == pr.getEntityType() && a.getOrgId() == pr.getEntityId())
//                    .findFirst();
//            pr.setEntityId(result.get().getId());
//
//            personRelationMapper.update(
//                    pr,
//                    new LambdaUpdateWrapper<PersonRelation>()
//                            .set(PersonRelation::getEntityId, pr.getEntityId())
//                            .eq(PersonRelation::getId, pr.getId())
//            );
//            System.out.println(STR."update success ep id: \{pr.getId()}, old related_id: \{result.get().getOrgId()}, new related_id: \{result.get().getId()}");
//        }
//    }



}
