package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.emun.Entity;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.emun.ItemType;
import com.rakbow.kureakurusu.data.vo.ItemDetailVO;
import com.rakbow.kureakurusu.service.ItemService;
import com.rakbow.kureakurusu.service.PersonService;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.ItemUtil;
import com.rakbow.kureakurusu.util.common.JsonUtil;
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
    private final PersonService personSrv;

//    @PostMapping("detail")
//    public ApiResult detail(@RequestBody CommonDetailQry qry) {
//        ItemDetailVO vo = srv.detail(qry.getId());
//        vo.setPersonnel(personSrv.getPersonnel(EntityType.ITEM.getValue(), qry.getId()));
//        return new ApiResult().load(vo);
//    }

    @PostMapping("detail")
    public ApiResult detail(@RequestBody CommonDetailQry qry) {
        return new ApiResult().load(srv.getById(qry.getId()));
    }

    @DeleteMapping("delete")
    public ApiResult delete(@RequestBody ItemDeleteDTO dto) {
        srv.delete(dto.getIds());
        return new ApiResult().ok(I18nHelper.getMessage("entity.crud.delete.success"));
    }

    @PostMapping("page")
    public ApiResult page(@RequestBody AlbumListParams qry) {
        return new ApiResult().load(srv.list(qry));
    }

    @PostMapping("test")
    public ApiResult test(@RequestBody AlbumUpdateDTO dto) {
        srv.test(dto);
        return new ApiResult().ok("");
    }

}