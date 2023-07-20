package com.rakbow.kureakurusu.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.ApiInfo;
import com.rakbow.kureakurusu.data.ApiResult;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-05-20 17:41
 * @Description:
 */
@Controller
public class GeneralController {

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String add(@RequestBody JSONObject json) {
        ApiResult res = new ApiResult();
        try {
            //检测数据
            String checkMsg = null;
            if(!StringUtils.isBlank(checkMsg)) {
                throw new Exception(checkMsg);
            }



        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return JSON.toJSONString(res);
    }

    //更新专辑基础信息
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String updateAlbum(@RequestBody JSONObject json) {
        ApiResult res = new ApiResult();
        try {
            String checkMsg = null;
            if(!StringUtils.isBlank(checkMsg)) {
                throw new Exception(checkMsg);
            }



        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return JSON.toJSONString(res);
    }

    //删除专辑(单个/多个)
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public String delete(@RequestBody JSONArray json) {
        ApiResult res = new ApiResult();
        try {
            List<Integer> ids = new ArrayList<>();
            for (int i = 0; i < json.size(); i++) {
                ids.add(json.getJSONObject(i).getIntValue("id"));
            }
            if(!ids.isEmpty()) {
                // //从数据库中删除专辑
                // albumService.deleteAlbums(ids);
                //
                // //删除专辑对应的music
                // musicService.deleteMusicsByAlbumIds(ids);
            }
            res.message = String.format(ApiInfo.DELETE_DATA_SUCCESS, Entity.ALBUM.getNameZh());
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return JSON.toJSONString(res);
    }

}
