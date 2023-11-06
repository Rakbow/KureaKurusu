package com.rakbow.kureakurusu.controller;

import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.ApiResult;
import com.rakbow.kureakurusu.data.MetaData;
import com.rakbow.kureakurusu.data.SimpleSearchParam;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.vo.person.PersonVOBeta;
import com.rakbow.kureakurusu.entity.Person;
import com.rakbow.kureakurusu.service.GeneralService;
import com.rakbow.kureakurusu.util.I18nHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-10-06 4:40
 * @Description:
 */
@Controller
@RequestMapping("/db")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Resource
    private GeneralService generalService;

    @RequestMapping(path = "/search-person", method = RequestMethod.POST)
    @ResponseBody
    public String searchPerson(@RequestBody JSONObject json) {
        ApiResult res = new ApiResult();
        try {
            res.data = generalService.searchPersons(new SimpleSearchParam(json));
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

    @RequestMapping(path = "/get-persons", method = RequestMethod.POST)
    @ResponseBody
    public String getPersons(@RequestBody JSONObject json) {
        ApiResult res = new ApiResult();
        try {
            res.data = generalService.getPersons(new QueryParams(json));
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

    @RequestMapping(path = "/add-person", method = RequestMethod.POST)
    @ResponseBody
    public String addPerson(@Valid @RequestBody Person person, BindingResult bindingResult) {
        ApiResult res = new ApiResult();
        try {
            if (bindingResult.hasErrors()) {
                List<FieldError> errors = bindingResult.getFieldErrors();
                res.setErrorMessage(errors);
                return res.toJson();
            }
            generalService.addPerson(person);
            res.message = I18nHelper.getMessage("entity.curd.insert.success", Entity.ALBUM.getNameZh());
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

    @RequestMapping(path = "/update-person", method = RequestMethod.POST)
    @ResponseBody
    public String updatePerson(@Valid @RequestBody PersonVOBeta person, BindingResult bindingResult) {
        ApiResult res = new ApiResult();
        try {
            if (bindingResult.hasErrors()) {
                List<FieldError> errors = bindingResult.getFieldErrors();
                res.setErrorMessage(errors);
                return res.toJson();
            }
            generalService.updatePerson(person);
            res.message = I18nHelper.getMessage("entity.curd.update.success", Entity.ALBUM.getNameZh());
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

    @RequestMapping(path = "/get-meta-data", method = RequestMethod.POST)
    @ResponseBody
    public String getMetaData() {
        ApiResult res = new ApiResult();
        try {
            res.data = MetaData.getOptions();
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

}
