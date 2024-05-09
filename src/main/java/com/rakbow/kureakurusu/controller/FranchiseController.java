package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.entity.Franchise;
import com.rakbow.kureakurusu.service.FranchiseService;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import com.rakbow.kureakurusu.toolkit.convert.FranchiseVOMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * @author Rakbow
 * @since 2023-01-14 17:07
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("db/franchise")
public class FranchiseController {

    //region inject

    private final FranchiseService srv;
    private final FranchiseVOMapper voMapper;

    //endregion

    //region crud

    @PostMapping("detail")
    @UniqueVisitor
    public ApiResult detail(@RequestBody FranchiseDetailQry qry) {
        return new ApiResult().load(srv.detail(qry));
    }

    @PostMapping("list")
    public ApiResult list(@RequestBody ListQueryDTO qry) {
        return new ApiResult().load(srv.list(new FranchiseListParams(qry)));
    }

    @PostMapping("add")
    public ApiResult add(@Valid @RequestBody FranchiseAddDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //build
        Franchise item = voMapper.build(dto);
        //save
        srv.save(item);
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.insert.success"));
    }

    @PostMapping("update")
    public ApiResult update(@Valid @RequestBody FranchiseUpdateDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //save
        srv.updateById(new Franchise(dto));
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.update.success"));
    }

    @DeleteMapping("delete")
    public ApiResult delete(@RequestBody FranchiseDeleteCmd cmd) {
        srv.delete(cmd.getIds());
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.delete.success"));
    }

    //endregion

}