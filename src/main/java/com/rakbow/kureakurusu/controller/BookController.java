package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.BookCreateDTO;
import com.rakbow.kureakurusu.data.dto.BookIsbnDTO;
import com.rakbow.kureakurusu.data.dto.BookUpdateDTO;
import com.rakbow.kureakurusu.data.dto.SearchQry;
import com.rakbow.kureakurusu.service.BookService;
import com.rakbow.kureakurusu.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // region basic crud

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
