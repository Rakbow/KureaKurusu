package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.AlbumUpdateDTO;
import com.rakbow.kureakurusu.data.emun.Entity;
import com.rakbow.kureakurusu.data.entity.Album;
import com.rakbow.kureakurusu.service.ItemService;
import com.rakbow.kureakurusu.util.I18nHelper;
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

    @PostMapping("update")
    public ApiResult update(@Valid @RequestBody AlbumUpdateDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //save
        srv.updateJoinTest(dto);
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.update.success", Entity.ALBUM.getName()));
    }

}
