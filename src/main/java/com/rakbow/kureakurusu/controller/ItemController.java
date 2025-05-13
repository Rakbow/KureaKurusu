package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.service.ItemService;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * @author Rakbow
 * @since 2024/4/11 4:06
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("db/item")
public class ItemController {

    private final ItemService srv;

    //region basic crud

    @PostMapping("add")
    public ApiResult add(@Valid @RequestBody ItemCreateDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //save
        srv.insert(dto);
        return new ApiResult().ok(I18nHelper.getMessage("entity.crud.insert.success"));
    }

    @PostMapping("update")
    public ApiResult update(@Valid @RequestBody ItemUpdateDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //update
        return new ApiResult().ok(srv.update(dto));
    }

    @DeleteMapping("delete")
    public ApiResult delete(@RequestBody ItemDeleteDTO dto) {
        return new ApiResult().ok(srv.delete(dto.getIds()));
    }

    //endregion

    //region query

    @UniqueVisitor
    @PostMapping("detail/{id}")
    public ApiResult detail(@PathVariable("id") long id) {
        return new ApiResult().load(srv.detail(id));
    }

    @PostMapping("search")
    public ApiResult search(@RequestBody ItemSearchParams param) {
        return new ApiResult().load(srv.search(param));
    }

    @PostMapping("list")
    public ApiResult list(@RequestBody ListQueryDTO dto) {
        return new ApiResult().load(srv.list(dto));
    }

    //endregion

    //region advance crud

    @PostMapping("advance-create")
    public ApiResult advanceCreate(@Valid @RequestBody ItemSuperCreateDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //save
        return new ApiResult().load(srv.advanceCreate(dto.getItem(), dto.getImages(), dto.getRelatedEntities(), dto.getGenerateThumb()));
    }

    //endregion

}