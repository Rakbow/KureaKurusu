package com.rakbow.kureakurusu.controller;

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
import com.rakbow.kureakurusu.util.convertMapper.BookVOMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * @author Rakbow
 * @since 2022-12-30 10:18
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("db/book")
public class BookController {

    //region inject

    private final BookService srv;
    private final PersonService personSrv;
    private final BookVOMapper VOMapper = BookVOMapper.INSTANCES;
    private final int ENTITY_VALUE = Entity.BOOK.getValue();

    // region basic crud
    @PostMapping("detail")
    @UniqueVisitor
    public ApiResult detail(@RequestBody CommonDetailQry qry) {
        BookDetailVO vo = srv.getDetail(qry);
        vo.setPersonnel(personSrv.getPersonnel(ENTITY_VALUE, qry.getId()));
        return  new ApiResult().load(vo);
    }

    @PostMapping("list")
    public ApiResult list(@RequestBody ListQry qry) {
        return new ApiResult().load(srv.getBooks(new QueryParams(qry)));
    }

    @PostMapping("search")
    public ApiResult search(@RequestBody SearchQry qry) {
        return new ApiResult().load(srv.searchBooks(qry));
    }

    @PostMapping("add")
    public ApiResult add(@Valid @RequestBody BookAddDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //build
        Book book = VOMapper.build(dto);
        //save
        srv.save(book);
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.insert.success", Entity.ALBUM.getName()));
    }

    @PostMapping("update")
    public ApiResult updateBook(@Valid @RequestBody BookUpdateDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //save
        srv.updateBook(dto);
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.update.success", Entity.BOOK.getName()));
    }

    @DeleteMapping("delete")
    public ApiResult deleteBook(@RequestBody DeleteCmd cmd) {
        srv.deleteBooks(cmd.getIds());
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.delete.success", Entity.BOOK.getName()));
    }

    //endregion

    //region other
    @PostMapping("get-isbn")
    public ApiResult getISBN(@RequestBody BookIsbnDTO dto) {
        return new ApiResult().load(srv.getISBN(dto.getLabel(), dto.getIsbn()));
    }

    //endregion

}
