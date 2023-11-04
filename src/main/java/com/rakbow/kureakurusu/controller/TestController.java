package com.rakbow.kureakurusu.controller;

import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.controller.entity.AlbumController;
import com.rakbow.kureakurusu.data.ApiResult;
import com.rakbow.kureakurusu.data.SimpleSearchParam;
import com.rakbow.kureakurusu.service.GeneralService;
import com.rakbow.kureakurusu.service.I18nService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Resource
    private GeneralService generalService;
    @Resource
    private I18nService i18nService;

    @RequestMapping(path = "/search-person", method = RequestMethod.POST)
    @ResponseBody
    public String searchPerson(@RequestBody JSONObject json) {
        ApiResult res = new ApiResult();
        try {
            res.data = generalService.searchPersons(new SimpleSearchParam(json));
            logger.info(i18nService.getMessage("test_key"));
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

}
