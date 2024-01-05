package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.ApiResult;
import com.rakbow.kureakurusu.data.dto.album.AlbumDetailQry;
import com.rakbow.kureakurusu.entity.Album;
import com.rakbow.kureakurusu.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rakbow
 * @since 2024/1/5 15:57
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final AlbumService service;


    @PostMapping("get-album")
    @UniqueVisitor
    public ApiResult getAlbum(@RequestBody AlbumDetailQry qry) {
        ApiResult res = new ApiResult();
        try {
            Album album = service.getAlbum(qry.getId());
            res.data = album;
        }catch (Exception e) {
            res.fail(e);
        }
        return res;
    }

}
