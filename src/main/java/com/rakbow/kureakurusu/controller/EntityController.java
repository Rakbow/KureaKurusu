package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.CommonDetailQry;
import com.rakbow.kureakurusu.data.vo.entry.EntryDetailVO;
import com.rakbow.kureakurusu.service.EntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rakbow
 * @since 2024/7/2 21:59
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("db")
public class EntityController {

    private final EntryService entrySrv;

    @PostMapping("entry/detail")
    @UniqueVisitor
    public ApiResult detail(@RequestBody CommonDetailQry qry) {
        return new ApiResult().load(entrySrv.detail(qry.getId()));
    }

}
