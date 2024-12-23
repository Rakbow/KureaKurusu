package com.rakbow.kureakurusu.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.SimpleSearchParam;
import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.entity.Item;
import com.rakbow.kureakurusu.service.ItemService;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import com.rakbow.kureakurusu.toolkit.ItemUtil;
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
        return new ApiResult().ok(srv.insert(dto));
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
    @PostMapping("detail")
    public ApiResult detail(@RequestBody CommonDetailQry qry) {
        return new ApiResult().load(srv.detail(qry.getId()));
    }

    @PostMapping("search")
    public ApiResult search(@RequestBody SearchQry qry) {
        return new ApiResult().load(srv.search(new SimpleSearchParam(qry)));
    }

    @PostMapping("list")
    public ApiResult list(@RequestBody ListQueryDTO dto) {
        return new ApiResult().load(srv.list(dto));
    }

    //endregion

    //region advance crud

    @PostMapping("update-bonus")
    public ApiResult updateItemBonus(@RequestBody ItemBonusUpdateDTO dto) {
        srv.update(new LambdaUpdateWrapper<Item>().eq(Item::getId, dto.getId()).set(Item::getBonus, dto.getBonus()));
        return new ApiResult().ok(I18nHelper.getMessage("entity.crud.bonus.update.success"));
    }

    //endregion

    //region other

    @GetMapping("get-option")
    public ApiResult getOption() {
        return new ApiResult().load(ItemUtil.getOptions());
    }

    //endregion

}