package com.rakbow.kureakurusu.controller.entity;

import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.system.ApiResult;
import com.rakbow.kureakurusu.service.EntryService;
import com.rakbow.kureakurusu.util.I18nHelper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Rakbow
 * @since 2023-05-03 1:49
 */
@Controller
@RequestMapping("/db/entry")
public class EntryController {

    @Resource
    private EntryService entryService;

    //region other
    @RequestMapping(value = "/refresh-redis-data", method = RequestMethod.POST)
    @ResponseBody
    public String refreshRedisEntryData(@RequestBody JSONObject json) {
        ApiResult res = new ApiResult();
        try {
            int entryCategory = json.getIntValue("entryCategory");
            entryService.refreshRedisEntries(entryCategory);
            res.message = I18nHelper.getMessage("redis.refresh_data.success");
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return res.toJson();
    }
    //endregion

}
