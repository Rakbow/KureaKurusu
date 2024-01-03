package com.rakbow.kureakurusu.controller.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.controller.interceptor.AuthorityInterceptor;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.vo.book.BookVOAlpha;
import com.rakbow.kureakurusu.entity.Book;
import com.rakbow.kureakurusu.service.*;
import com.rakbow.kureakurusu.data.ApiInfo;
import com.rakbow.kureakurusu.data.ApiResult;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.common.EntityUtil;
import com.rakbow.kureakurusu.util.convertMapper.entity.BookVOMapper;
import com.rakbow.kureakurusu.util.file.CommonImageUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2022-12-30 10:18
 */

@Controller
@RequestMapping("/db/book")
public class BookController {

    //region ------引入实例------

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Resource
    private BookService bookService;
    @Resource
    private EntityUtil entityUtil;
    @Resource
    private EntityService entityService;
    

    private final BookVOMapper bookVOMapper = BookVOMapper.INSTANCES;

    //endregion

    //region ------获取页面------

    //获取单个图书详细信息页面
    // @UniqueVisitor
    // @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    // public String getBookDetail(@PathVariable("id") Integer id, Model model) {
    //     Book book = bookService.getBookWithAuth(id);
    //     if (book == null) {
    //         model.addAttribute("errorMessage", String.format(ApiInfo.GET_DATA_FAILED_404, Entity.BOOK.getNameZh()));
    //         return "/error/404";
    //     }
    //     model.addAttribute("book", bookService.buildVO(book));
    //     if(AuthorityInterceptor.isJunior()) {
    //         //前端选项数据
    //         model.addAttribute("options", entityUtil.getDetailOptions(Entity.BOOK.getId()));
    //     }
    //     //实体类通用信息
    //     model.addAttribute("detailInfo", entityUtil.getItemDetailInfo(book, Entity.BOOK.getId()));
    //     //获取页面数据
    //     model.addAttribute("pageInfo", entityService.getPageInfo(Entity.BOOK.getId(), id, book));
    //     //图片相关
    //     model.addAttribute("itemImageInfo", CommonImageUtil.segmentImages(book.getImages(), 200, Entity.BOOK, false));
    //     //获取相关图书
    //     // model.addAttribute("relatedBooks", bookService.getRelatedBooks(id));
    //     return "/database/itemDetail/book-detail";
    // }

    //endregion

    //region ------增删改查------

    //新增图书
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addBook(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            JSONObject param = JSON.parseObject(json);
            //检测数据
            String errorMsg = bookService.checkBookJson(param);
            if(!StringUtils.isBlank(errorMsg)) {
                res.setErrorMessage(errorMsg);
                return res.toJson();
            }

            Book book = entityService.json2Entity(bookService.handleBookJson(param), Book.class);

            //保存新增图书
            res.message = bookService.addBook(book);
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return res.toJson();
    }

    //删除图书(单个/多个)
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteBook(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            List<Book> books = JSON.parseArray(json).toJavaList(Book.class);
            for (Book book : books) {
                //从数据库中删除专辑
                bookService.deleteBook(book);
            }
            res.message = I18nHelper.getMessage("entity.curd.delete.success", Entity.BOOK.getNameZh());
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return res.toJson();
    }

    //更新图书基础信息
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String updateBook(@RequestBody String json) {
        ApiResult res = new ApiResult();
        JSONObject param = JSON.parseObject(json);
        try {
            //检测数据
            String errorMsg = bookService.checkBookJson(param);
            if(!StringUtils.isBlank(errorMsg)) {
                res.setErrorMessage(errorMsg);
                return res.toJson();
            }

            Book book = entityService.json2Entity(bookService.handleBookJson(param), Book.class);

            //修改编辑时间
            book.setEditedTime(DateHelper.NOW_TIMESTAMP);

            res.message = bookService.updateBook(book.getId(), book);
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return res.toJson();
    }

    //endregion

    //region ------进阶信息增删改查------

    //根据搜索条件获取图书--列表界面
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/get-books", method = RequestMethod.POST)
     @ResponseBody
     public String getBooksByFilterList(@RequestBody String json, HttpServletRequest request) {
        JSONObject param = JSON.parseObject(json);
        QueryParams queryParam = JSON.to(QueryParams.class, param.getJSONObject("queryParams"));
        String pageLabel = param.getString("pageLabel");

        List<BookVOAlpha> books = new ArrayList<>();

        SearchResult searchResult = bookService.getBooksByFilter(queryParam);

        if (StringUtils.equals(pageLabel, "list")) {
            books = bookVOMapper.book2VOAlpha((List<Book>) searchResult.data);
        }
        if (StringUtils.equals(pageLabel, "index")) {
            books = bookVOMapper.book2VOAlpha((List<Book>) searchResult.data);
        }

        JSONObject result = new JSONObject();
        result.put("data", books);
        result.put("total", searchResult.total);

        return JSON.toJSONString(result);
     }

    //更新图书作者信息
    @RequestMapping(path = "/update-authors", method = RequestMethod.POST)
    @ResponseBody
    public String updateBookAuthors(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            int id = JSON.parseObject(json).getInteger("id");
            String authors = JSON.parseObject(json).getJSONArray("authors").toString();
            if (StringUtils.isBlank(authors)) {
                res.setErrorMessage(I18nHelper.getMessage("entity.crud.input.empty"));
                return res.toJson();
            }

            res.message = bookService.updateBookAuthors(id, authors);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

    //endregion

    //region other

    //isbn互相转换
    @RequestMapping(value = "/get-isbn", method = RequestMethod.POST)
    @ResponseBody
    public String getISBN(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {

            JSONObject param = JSON.parseObject(json);
            String label = param.getString("label");
            String isbn = param.getString("isbn");

            res.data = bookService.getISBN(label, isbn);

        }catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

    @RequestMapping(value = "/get-related-books", method = RequestMethod.POST)
    @ResponseBody
    public String getRelatedBooks(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {

            JSONObject param = JSON.parseObject(json);
            int id = param.getInteger("id");
            res.data = bookService.getRelatedBooks(id);

        }catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

    //endregion

}
