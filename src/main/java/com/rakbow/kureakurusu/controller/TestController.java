package com.rakbow.kureakurusu.controller;

import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.ApiResult;
import com.rakbow.kureakurusu.service.GeneralService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-10-06 4:40
 * @Description:
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @Resource
    private GeneralService generalService;

    @RequestMapping(path = "/search-person", method = RequestMethod.POST)
    @ResponseBody
    public String searchPerson(@RequestBody JSONObject json) {
        ApiResult res = new ApiResult();
        try {
            String keyword = json.getString("keyword");
            int first = json.getIntValue("first");
            int row = json.getIntValue("row");
            res.data = generalService.searchPersons(keyword, first, row);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

}
