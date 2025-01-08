package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.SimpleSearchParam;
import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.EntryUpdateDTO;
import com.rakbow.kureakurusu.data.dto.GeneralSearchQry;
import com.rakbow.kureakurusu.data.dto.ImageCreateDTO;
import com.rakbow.kureakurusu.service.EntryService;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * @author Rakbow
 * @since 2024/7/2 21:59
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("db/entry")
public class EntryController {

    private final EntryService srv;

    @UniqueVisitor
    @PostMapping("detail/{type}/{id}")
    public ApiResult detail(@PathVariable("type") int type, @PathVariable("id") long id) {
        return new ApiResult().load(srv.detail(type, id));
    }

    @PostMapping("update")
    public ApiResult update(@Valid @RequestBody EntryUpdateDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //update
        return new ApiResult().ok(srv.update(dto));
    }

    @PostMapping("search")
    public ApiResult search(@RequestBody GeneralSearchQry qry) {
        return new ApiResult().load(srv.search(qry.getEntrySearchType(), new SimpleSearchParam(qry.getParam())));
    }

    @PostMapping("upload-image")
    public ApiResult uploadImage(@RequestBody ImageCreateDTO dto) {
        ApiResult res = new ApiResult();
        //check
        if (dto.getImages().isEmpty()) return res.fail(I18nHelper.getMessage("file.empty"));
        res.load(srv.uploadImage(dto.getEntityType(), dto.getEntityId(), dto.getImages().getFirst()));
        return res.ok(I18nHelper.getMessage("image.update.success"));
    }

}
