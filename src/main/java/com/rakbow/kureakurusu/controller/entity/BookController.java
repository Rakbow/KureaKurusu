package com.rakbow.kureakurusu.controller.entity;

import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.dto.base.CommonDetailQry;
import com.rakbow.kureakurusu.data.dto.base.ListQry;
import com.rakbow.kureakurusu.data.dto.base.SearchQry;
import com.rakbow.kureakurusu.data.dto.book.BookAddDTO;
import com.rakbow.kureakurusu.data.dto.book.BookIsbnDTO;
import com.rakbow.kureakurusu.data.dto.book.BookUpdateDTO;
import com.rakbow.kureakurusu.data.dto.common.DeleteCmd;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.entity.Book;
import com.rakbow.kureakurusu.data.system.ApiResult;
import com.rakbow.kureakurusu.data.vo.book.BookDetailVO;
import com.rakbow.kureakurusu.service.BookService;
import com.rakbow.kureakurusu.service.PersonService;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.convertMapper.entity.BookVOMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * @author Rakbow
 * @since 2022-12-30 10:18
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/db/book")
public class BookController {

    //region ------引入实例------

    private static final Logger log = LoggerFactory.getLogger(BookController.class);
    private final BookService srv;
    private final PersonService personSrv;
    private final BookVOMapper VOMapper = BookVOMapper.INSTANCES;
    private final int ENTITY_VALUE = Entity.BOOK.getValue();

    // region ------basic crud------
    @PostMapping("detail")
    @UniqueVisitor
    public ApiResult getDetail(@RequestBody CommonDetailQry qry) {
        ApiResult res = new ApiResult();
        try {
            BookDetailVO vo = srv.getDetail(qry);
            vo.setPersonnel(personSrv.getPersonnel(ENTITY_VALUE, qry.getId()));
            res.loadData(vo);
        }catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    @PostMapping("list")
    public ApiResult list(@RequestBody ListQry qry) {
        ApiResult res = new ApiResult();
        try{
            res.loadData(srv.getBooks(new QueryParams(qry)));
        }catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    @PostMapping("search")
    public ApiResult search(@RequestBody SearchQry qry) {
        ApiResult res = new ApiResult();
        try {
            res.data = srv.searchBooks(qry);
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    @PostMapping("add")
    public ApiResult addBook(@Valid @RequestBody BookAddDTO dto, BindingResult errors) {
        ApiResult res = new ApiResult();
        try {
            //check
            if (errors.hasErrors())
                return res.fail(errors);
            //build
            Book book = VOMapper.build(dto);
            //save
            srv.save(book);
            res.ok(I18nHelper.getMessage("entity.curd.insert.success", Entity.ALBUM.getName()));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    @PostMapping("update")
    public ApiResult updateBook(@Valid @RequestBody BookUpdateDTO dto, BindingResult errors) {
        ApiResult res = new ApiResult();
        try {
            //check
            if (errors.hasErrors())
                return res.fail(errors);
            //save
            srv.updateBook(dto);
            res.ok(I18nHelper.getMessage("entity.curd.update.success", Entity.BOOK.getName()));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    @DeleteMapping("delete")
    public ApiResult deleteBook(@RequestBody DeleteCmd cmd) {
        ApiResult res = new ApiResult();
        try {
            srv.deleteBooks(cmd.getIds());
            res.ok(I18nHelper.getMessage("entity.curd.delete.success", Entity.BOOK.getName()));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    //endregion

    //region other

    //isbn互相转换
    @PostMapping("get-isbn")
    public ApiResult getISBN(@RequestBody BookIsbnDTO dto) {
        ApiResult res = new ApiResult();
        try {
            res.loadData(srv.getISBN(dto.getLabel(), dto.getIsbn()));
        }catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage(), e);
        }
        return res;
    }

    //endregion

}
