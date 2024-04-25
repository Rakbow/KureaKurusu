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
import com.rakbow.kureakurusu.util.common.JsonUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("update")
    public ApiResult update(@Valid @RequestBody String param, BindingResult errors) {
        int type = JsonUtil.getIntValueByKey("type", param);
        ItemUpdateDTO dto = null;
        if(type == ItemType.ALBUM.getValue()){
            dto = JsonUtil.to(param, AlbumItemUpdateDTO.class);
        }
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //save
        assert dto != null;
        srv.update(dto);
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.update.success", Entity.ALBUM.getName()));
    }

    @PostMapping("page")
    public ApiResult page(@RequestBody AlbumListParams qry) {
        return new ApiResult().load(srv.list(qry));
    }

}