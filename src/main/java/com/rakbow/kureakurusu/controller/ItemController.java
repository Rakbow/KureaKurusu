package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.data.SimpleSearchParam;
import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.CommonDetailQry;
import com.rakbow.kureakurusu.data.dto.ItemDeleteDTO;
import com.rakbow.kureakurusu.data.dto.ListQueryDTO;
import com.rakbow.kureakurusu.data.dto.SearchQry;
import com.rakbow.kureakurusu.service.ItemService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("detail")
    public ApiResult detail(@RequestBody CommonDetailQry qry) {
        return new ApiResult().load(srv.detail(qry.getId()));
    }

    @PostMapping("search")
    public ApiResult search(@RequestBody SearchQry qry) {
        return new ApiResult().load(srv.search(new SimpleSearchParam(qry)));
    }

    @DeleteMapping("delete")
    public ApiResult delete(@RequestBody ItemDeleteDTO dto) {
        return new ApiResult().ok(srv.delete(dto.getIds()));
    }

    @PostMapping("list")
    public ApiResult list(@RequestBody ListQueryDTO dto) {
        return new ApiResult().load(srv.list(dto));
    }

}