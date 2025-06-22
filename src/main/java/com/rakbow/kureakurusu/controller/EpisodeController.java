package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.EpisodeRelatedDTO;
import com.rakbow.kureakurusu.data.dto.ListQuery;
import com.rakbow.kureakurusu.service.EpisodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author Rakbow
 * @since 2024/1/11 15:11
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("db/ep")
public class EpisodeController {

    private final EpisodeService srv;

    @UniqueVisitor
    @PostMapping("detail/{id}")
    public ApiResult detail(@PathVariable("id") long id) {
        return new ApiResult().load(srv.detail(id));
    }

    @PostMapping("list")
    public ApiResult list(@RequestBody ListQuery dto) {
        return new ApiResult().load(srv.list(dto));
    }

    @PostMapping("related")
    public ApiResult related(@RequestBody EpisodeRelatedDTO dto) {
        return new ApiResult().load(srv.related(dto));
    }

}
