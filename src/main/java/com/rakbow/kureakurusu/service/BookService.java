package com.rakbow.kureakurusu.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.controller.interceptor.AuthorityInterceptor;
import com.rakbow.kureakurusu.dao.BookMapper;
import com.rakbow.kureakurusu.data.ApiInfo;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.vo.book.BookVO;
import com.rakbow.kureakurusu.data.vo.book.BookVOBeta;
import com.rakbow.kureakurusu.entity.Book;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.CommonUtil;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.common.VisitUtil;
import com.rakbow.kureakurusu.util.convertMapper.entity.BookVOMapper;
import com.rakbow.kureakurusu.util.entity.BookUtil;
import com.rakbow.kureakurusu.util.file.QiniuFileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2022-12-28 23:45 book业务层
 */
@Service
public class BookService {

    //region ------注入依赖------

    @Resource
    private BookMapper bookMapper;
    @Resource
    private QiniuFileUtil qiniuFileUtil;
    @Resource
    private VisitUtil visitUtil;
    

    private final BookVOMapper bookVOMapper = BookVOMapper.INSTANCES;

    //endregion

    //region ------CRUD------

    /**
     * 新增图书
     *
     * @param book 新增的图书
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String addBook(Book book) {
        int id = bookMapper.addBook(book);
        return I18nHelper.getMessage("entity.curd.insert.success", Entity.BOOK.getNameZh());
    }

    /**
     * 根据Id获取图书,泛用
     *
     * @param id 图书id
     * @return book
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public Book getBook(int id) {
        return bookMapper.getBook(id, true);
    }

     /**
      * 根据Id获取图书,需要判断权限
      *
      * @param id 图书id
      * @return book
      * @author rakbow
      */
     @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
     public Book getBookWithAuth(int id) {
         if(AuthorityInterceptor.isSenior()) {
             return bookMapper.getBook(id, true);
         }
         return bookMapper.getBook(id, false);
     }

    /**
     * 根据Id删除图书
     *
     * @param book 图书
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void deleteBook(Book book) {
        //删除前先把服务器上对应图片全部删除
        qiniuFileUtil.commonDeleteAllFiles(JSON.parseArray(book.getImages()));
        bookMapper.deleteBook(book.getId());
        visitUtil.deleteVisit(Entity.BOOK.getId(), book.getId());
    }

    /**
     * 更新图书基础信息
     *
     * @param id 图书id
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String updateBook(int id, Book book) {
        bookMapper.updateBook(id, book);
        return I18nHelper.getMessage("entity.curd.update.success", Entity.BOOK.getNameZh());
    }

    //endregion

    //region ------数据处理------

    public BookVO buildVO(Book book) {
        BookVO VO = bookVOMapper.toVO(book);
        if(AuthorityInterceptor.isJunior()) {
            //可供编辑的editCompanies
            VO.setEditCompanies(JSON.parseArray(book.getCompanies()));
            //可供编辑的editPersonnel
            VO.setEditPersonnel(JSON.parseArray(book.getPersonnel()));
            //可供编辑的editSpecs
            VO.setEditSpecs(JSON.parseArray(book.getSpecs()));
        }
        return VO;
    }

    /**
     * 检测数据合法性
     *
     * @param bookJson bookJson
     * @return string类型错误消息，若为空则数据检测通过
     * @author rakbow
     */
    public String checkBookJson(JSONObject bookJson) {
        if (StringUtils.isBlank(bookJson.getString("title"))) {
            return I18nHelper.getMessage("entity.crud.name.required_field");
        }

        if (!StringUtils.isBlank(bookJson.getString("isbn10"))) {
            if (bookJson.getString("isbn10").replaceAll("-", "").length() != 10) {
                return I18nHelper.getMessage("book.crud.isbn10.invalid");
            }
        } else {
            return I18nHelper.getMessage("book.crud.isbn10.invalid");
        }
        if (!StringUtils.isBlank(bookJson.getString("isbn13"))) {
            if (bookJson.getString("isbn13").replaceAll("-", "").length() != 13) {
                return I18nHelper.getMessage("book.crud.isbn13.invalid");
            }
        } else {
            return I18nHelper.getMessage("book.crud.isbn13.invalid");
        }
        if (StringUtils.isBlank(bookJson.getString("publishDate"))) {
            return I18nHelper.getMessage("entity.crud.publish_date.required_field");
        }
        if (StringUtils.isBlank(bookJson.getString("bookType"))) {
            return I18nHelper.getMessage("entity.crud.category.required_field");
        }
        if (StringUtils.isBlank(bookJson.getString("franchises"))
                || StringUtils.equals(bookJson.getString("franchises"), "[]")) {
            return I18nHelper.getMessage("entity.crud.category.required_field");
        }
        if (StringUtils.isBlank(bookJson.getString("products"))
                || StringUtils.equals(bookJson.getString("products"), "[]")) {
            return I18nHelper.getMessage("entity.crud.product.required_field");
        }
        return "";
    }

    /**
     * 处理前端传送图书数据
     *
     * @param bookJson bookJson
     * @return 处理后的book json格式数据
     * @author rakbow
     */
    public JSONObject handleBookJson(JSONObject bookJson) {

        String[] products = CommonUtil.str2SortedArray(bookJson.getString("products"));
        String[] franchises = CommonUtil.str2SortedArray(bookJson.getString("franchises"));

        bookJson.put("isbn10", bookJson.getString("isbn10").replaceAll("-", ""));
        bookJson.put("isbn13", bookJson.getString("isbn13").replaceAll("-", ""));
        bookJson.put("publishDate", bookJson.getDate("publishDate"));
        bookJson.put("products", "{\"ids\":[" + StringUtils.join(products, ",") + "]}");
        bookJson.put("franchises", "{\"ids\":[" + StringUtils.join(franchises, ",") + "]}");

        return bookJson;
    }

    //endregion

    //region ------更新book数据------

    /**
     * 更新图书作者信息
     *
     * @param id      图书id
     * @param authors 图书的作者信息json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String updateBookAuthors(int id, String authors) {
        bookMapper.updateBookAuthors(id, authors, DateHelper.NOW_TIMESTAMP);
        return I18nHelper.getMessage("entity.crud.personnel.update.success");
    }

    //endregion

    //region ------特殊查询------

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public SearchResult getBooksByFilter(QueryParams param) {

        JSONObject filter = param.getFilters();

        String title = filter.getJSONObject("title").getString("value");
        String isbn10 = filter.getJSONObject("isbn10").getString("value");
        String isbn13 = filter.getJSONObject("isbn13").getString("value");
        String region = filter.getJSONObject("region").getString("value");
        String publishLanguage = filter.getJSONObject("publishLanguage").getString("value");

        int bookType = 100;
        if (filter.getJSONObject("bookType").get("value") != null) {
            bookType = filter.getJSONObject("bookType").getIntValue("value");
        }

        List<Integer> franchises = filter.getJSONObject("franchises").getList("value", Integer.class);
        List<Integer> products = filter.getJSONObject("products").getList("value", Integer.class);
        List<Integer> serials = filter.getJSONObject("serials").getList("value", Integer.class);

        String hasBonus;
        if (filter.getJSONObject("hasBonus").get("value") == null) {
            hasBonus = null;
        } else {
            hasBonus = filter.getJSONObject("hasBonus").getBoolean("value")
                    ? Integer.toString(1) : Integer.toString(0);
        }

        List<Book> books  = new ArrayList<>();
        // List<Book> books = bookMapper.getBooksByFilter(title, isbn10, isbn13, serials, region, publishLanguage,
        //         bookType, franchises, products, hasBonus, AuthorityInterceptor.isSenior(), param.getSortField(), param.getSortOrder(), param.getFirst(), param.getRows());

        int total = bookMapper.getBooksRowsByFilter(title, isbn10, isbn13, serials, region, publishLanguage,
                bookType, franchises, products, hasBonus, AuthorityInterceptor.isSenior());

        return new SearchResult(books, total);
    }

    /**
     * 根据作品id获取图书
     *
     * @param productId 作品id
     * @return List<JSONObject>
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<BookVOBeta> getBooksByProductId(int productId) {
        List<Integer> products = new ArrayList<>();
        products.add(productId);

        List<Book> books = bookMapper.getBooksByFilter(null, null, null, null,
                null, null, 100, null, products, null, false, "publishDate",
                -1,  0, 0);

        return bookVOMapper.book2VOBeta(books);
    }

    /**
     * 获取相关联Book
     *
     * @param id 图书id
     * @return list封装的Book
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<BookVOBeta> getRelatedBooks(int id) {

        List<Book> result = new ArrayList<>();

//        Book book = getBook(id);
//
//        //该Book包含的作品id
//        List<Integer> productIds = JSONObject.parseObject(book.getProducts()).getList("ids", Integer.class);
//
//        //该系列所有Book
//        List<Book> allBooks = bookMapper.getBooksByFilter(null, null, null, 0,
//                        null, null, 100, CommonUtil.ids2List(book.getFranchises()),
//                        null, null, false, "publishDate", 1, 0, 0)
//                .stream().filter(tmpBook -> tmpBook.getId() != book.getId()).collect(Collectors.toList());
//
//        List<Book> queryResult = allBooks.stream().filter(tmpBook ->
//                StringUtils.equals(tmpBook.getProducts(), book.getProducts())
//                        && StringUtils.equals(tmpBook.getPublisher(), book.getPublisher())).collect(Collectors.toList());
//
//        if (queryResult.size() > 5) {//结果大于5
//            result.addAll(queryResult.subList(0, 5));
//        } else if (queryResult.size() == 5) {//结果等于5
//            result.addAll(queryResult);
//        } else if (queryResult.size() > 0) {//结果小于5不为空
//            List<Book> tmp = new ArrayList<>(queryResult);
//
//            if (productIds.size() > 1) {
//                List<Book> tmpQueryResult = allBooks.stream().filter(tmpBook ->
//                        JSONObject.parseObject(tmpBook.getProducts()).getList("ids", Integer.class)
//                                .contains(productIds.get(1))).collect(Collectors.toList());
//
//                if (tmpQueryResult.size() >= 5 - queryResult.size()) {
//                    tmp.addAll(tmpQueryResult.subList(0, 5 - queryResult.size()));
//                } else if (tmpQueryResult.size() > 0 && tmpQueryResult.size() < 5 - queryResult.size()) {
//                    tmp.addAll(tmpQueryResult);
//                }
//            }
//            result.addAll(tmp);
//        } else {
//            List<Book> tmp = new ArrayList<>(queryResult);
//            for (int productId : productIds) {
//                tmp.addAll(
//                        allBooks.stream().filter(tmpBook ->
//                                JSONObject.parseObject(tmpBook.getProducts()).getList("ids", Integer.class)
//                                        .contains(productId)).collect(Collectors.toList())
//                );
//            }
//            result = CommonUtil.removeDuplicateList(tmp);
//            if (result.size() >= 5) {
//                result = result.subList(0, 5);
//            }
//        }

        return bookVOMapper.book2VOBeta(CommonUtil.removeDuplicateList(result));
    }

    //endregion

    //region other

    /**
     * isbn互相转换
     *
     * @param label,isbn 转换方式,isbn
     * @return isbn
     * @author rakbow
     */
    public String getISBN(String label, String isbn) throws Exception {

        isbn = isbn.replaceAll("-", "");

        if(StringUtils.equals(label, "isbn13")) {
            if(isbn.length() != 10) {
                throw new Exception(I18nHelper.getMessage("book.crud.isbn10.invalid"));
            }
            return BookUtil.getISBN13(isbn);
        }
        if(StringUtils.equals(label, "isbn10")) {
            if(isbn.length() != 13) {
                throw new Exception(I18nHelper.getMessage("book.crud.isbn13.invalid"));
            }
            return BookUtil.getISBN10(isbn);
        }
        return null;
    }

    //endregion

}
