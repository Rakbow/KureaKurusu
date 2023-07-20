package com.rakbow.kureakurusu;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.dao.*;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.emun.entry.EntryCategory;
import com.rakbow.kureakurusu.data.emun.temp.BookRelatedType;
import com.rakbow.kureakurusu.data.emun.temp.EnumUtil;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.data.vo.album.AlbumVOAlpha;
import com.rakbow.kureakurusu.entity.Album;
import com.rakbow.kureakurusu.entity.Book;
import com.rakbow.kureakurusu.entity.Entry;
import com.rakbow.kureakurusu.entity.Product;
import com.rakbow.kureakurusu.service.AlbumService;
import com.rakbow.kureakurusu.service.EntityService;
import com.rakbow.kureakurusu.service.MusicService;
import com.rakbow.kureakurusu.util.common.LikeUtil;
import com.rakbow.kureakurusu.util.common.VisitUtil;
import com.rakbow.kureakurusu.util.convertMapper.entity.AlbumVOMapper;
import com.rakbow.kureakurusu.util.convertMapper.entry.EntryConvertMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//import com.rakbow.kureakurusu.util.convertMapper.GameVoMapper;

@SpringBootTest
class kureakurusuApplicationTests {

    @Resource
    private AlbumService albumService;
    @Resource
    private EntityService entityService;
    @Resource
    private MusicService musicService;
    @Resource
    private MusicMapper musicMapper;

    @Resource
    private ProductMapper productMapper;

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
    private FranchiseMapper franchiseMapper;
    @Resource
    private EntityMapper entityMapper;

    @Resource
    private LikeUtil likeUtil;
    @Resource
    private VisitUtil visitUtil;
    @Resource
    private StatisticMapper statisticMapper;
    @Resource
    private EntryMapper entryMapper;

    private final EntryConvertMapper entryConvertMapper = EntryConvertMapper.INSTANCES;

    private final AlbumVOMapper albumVOMapper = AlbumVOMapper.INSTANCES;

    @Test
    public void itemImageCovert() {
        List<Product> products = productMapper.getAll();
        products.forEach(product -> {
            JSONArray imageJsons = JSON.parseArray(product.getImages());
            if(!imageJsons.isEmpty()) {
                List<Image> images = new ArrayList<>();
                for (int i = 0; i < imageJsons.size(); i++) {
                    JSONObject imageJson = imageJsons.getJSONObject(i);
                    Image image = new Image();
                    image.setUrl(imageJson.getString("url"));
                    image.setNameZh(imageJson.getString("nameZh"));
                    image.setNameEn(imageJson.getString("nameEn"));
                    image.setType(imageJson.getIntValue("type"));
                    image.setDescription(imageJson.getString("description"));
                    image.setUploadTime(imageJson.getString("uploadTime"));
                    images.add(image);
                }
//                entityMapper.updateItemImages(Entity.PRODUCT.getNameEn().toLowerCase(), product.getId(), JSON.toJSONString(images), product.getEditedTime());
            }
        });

    }

    @Test
    public void mapStructTest() {

        List<Album> albums = albumMapper.getAll();

        long t1 = System.currentTimeMillis();

        List<AlbumVOAlpha> VOs = albumVOMapper.toVOAlpha(albums);

        long t2 = System.currentTimeMillis();
        
        System.out.println(t2 - t1);
    }

    @Test
    public void entryTests() {
        List<Entry> entries = entryMapper.getEntryByCategory(EntryCategory.PERSONNEL.getId());
    }

    @Test
    public void entryTest1() {
        List<Book> books = bookMapper.getAll();
        int k = 134;
        books.forEach(book -> {
            //update companies
            JSONArray companies = JSON.parseArray(book.getCompanies());
            if(!companies.isEmpty()) {
                JSONArray newCompanies = new JSONArray();
                for (int i = 0; i < companies.size(); i++) {
                    JSONObject company = companies.getJSONObject(i);
                    List<Integer> members = company.getJSONArray("members").toJavaList(Integer.class);
                    members.replaceAll(integer -> integer + k);
                    company.put("members", members);
                    newCompanies.add(company);
                }
                entityMapper.updateItemCompanies("book", book.getId(), newCompanies.toJSONString(), book.getEditedTime());
            }

            //update artists
            JSONArray allPersonnel = JSON.parseArray(book.getPersonnel());
            if(!allPersonnel.isEmpty()) {
                JSONArray newPersonnel = new JSONArray();
                for (int i = 0; i < allPersonnel.size(); i++) {
                    JSONObject personnel = allPersonnel.getJSONObject(i);

                    int role = personnel.getIntValue("role");
                    role += k;
                    personnel.put("role", role);

                    List<Integer> members = personnel.getJSONArray("members").toJavaList(Integer.class);
                    members.replaceAll(integer -> integer + k);
                    personnel.put("members", members);

                    newPersonnel.add(personnel);
                }
                entityMapper.updateItemPersonnel("book", "personnel", book.getId(), newPersonnel.toJSONString(), book.getEditedTime());
            }

            //update spec
            JSONArray specs = JSON.parseArray(book.getSpecs());
            if(!specs.isEmpty()) {
                JSONArray newSpecs = new JSONArray();
                for (int i = 0; i < specs.size(); i++) {
                    JSONObject spec = specs.getJSONObject(i);

                    int label = spec.getIntValue("label");
                    label += k;
                    spec.put("label", label);

                    newSpecs.add(spec);
                }
                entityMapper.updateItemSpecs("book", book.getId(), newSpecs.toJSONString(), book.getEditedTime());
            }

            //update products
            JSONObject products = JSONObject.parseObject(book.getProducts());
            if(!products.isEmpty()) {
                List<Integer> ids = products.getJSONArray("ids").toJavaList(Integer.class);
                ids.replaceAll(integer -> integer + 15);
                products.put("ids", ids);
                book.setProducts(products.toJSONString());
                bookMapper.updateBook(book.getId(), book);
            }

        });
    }

    @Test
    public void languageTest() {
        System.out.println(LocaleContextHolder.getLocale().getLanguage());
        System.out.println(Locale.JAPANESE.getLanguage());
        System.out.println(Locale.CHINESE.getLanguage());
        System.out.println(Locale.SIMPLIFIED_CHINESE.getLanguage());
        System.out.println(Locale.TRADITIONAL_CHINESE.getLanguage());
        System.out.println(Locale.ENGLISH.getLanguage());
    }

    @Test
    public void enumTest() {
        try{
        String idsJson = "[2, 4, 5, 7]";
//
        long t1 = System.currentTimeMillis();
//        List<Attribute> res1 = BookRelatedType.getAttributes(idsJson);
//            System.out.println(BookRelatedType.getAttribute(5));
        long t2 = System.currentTimeMillis();
        System.out.println(t2 - t1);
//        res1.forEach(System.out::println);
//
        long t3 = System.currentTimeMillis();
//        System.out.println(EnumUtil.getAttribute(BookRelatedType.class, 5));
        List<Attribute> res2 = EnumUtil.getAttributes(BookRelatedType.class, idsJson);
        long t4 = System.currentTimeMillis();
        System.out.println(t4 - t3);
//        res2.forEach(System.out::println);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
