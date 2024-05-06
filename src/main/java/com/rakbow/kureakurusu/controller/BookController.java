package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.emun.Entity;
import com.rakbow.kureakurusu.data.entity.Book;
import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.vo.book.BookDetailVO;
import com.rakbow.kureakurusu.service.BookService;
import com.rakbow.kureakurusu.service.ItemService;
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
    private final ItemService itemSrv;
    private final PersonService personSrv;
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
    public ApiResult list(@RequestBody ListQueryDTO qry) {
        return new ApiResult().load(itemSrv.list(new BookListQueryDTO(qry)));
    }

    @PostMapping("search")
    public ApiResult search(@RequestBody SearchQry qry) {
        return new ApiResult().load(srv.searchBooks(qry));
    }

    @PostMapping("add")
    public ApiResult add(@Valid @RequestBody BookCreateDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //save
        return new ApiResult().ok(itemSrv.insert(dto));
    }

    @PostMapping("update")
    public ApiResult updateBook(@Valid @RequestBody BookUpdateDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //save
        return  new ApiResult().ok(itemSrv.update(dto));
    }

    //endregion

    //region other
    @PostMapping("get-isbn")
    public ApiResult getISBN(@RequestBody BookIsbnDTO dto) {
        return new ApiResult().load(srv.getISBN(dto.getLabel(), dto.getIsbn()));
    }

    //endregion

}
