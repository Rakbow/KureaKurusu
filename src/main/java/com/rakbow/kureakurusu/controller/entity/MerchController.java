package com.rakbow.kureakurusu.controller.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.vo.merch.MerchVOAlpha;
import com.rakbow.kureakurusu.entity.Merch;
import com.rakbow.kureakurusu.service.*;
import com.rakbow.kureakurusu.data.ApiInfo;
import com.rakbow.kureakurusu.data.ApiResult;
import com.rakbow.kureakurusu.util.common.DateUtil;
import com.rakbow.kureakurusu.util.common.EntityUtil;
import com.rakbow.kureakurusu.util.convertMapper.entity.MerchVOMapper;
import com.rakbow.kureakurusu.util.file.CommonImageUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-01-04 16:57
 * @Description:
 */

@Controller
@RequestMapping("/db/merch")
public class MerchController {

    //region ------引入实例------

    private static final Logger logger = LoggerFactory.getLogger(MerchController.class);

    @Resource
    private MerchService merchService;
    @Resource
    private UserService userService;
    @Resource
    private EntityUtil entityUtil;
    @Resource
    private EntityService entityService;

    private final MerchVOMapper merchVOMapper = MerchVOMapper.INSTANCES;

    //endregion

    //region ------获取页面------

    //获取单个周边商品详细信息页面
//    @UniqueVisitor
//    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
//    public String getMerchDetail(@PathVariable int id, Model model) {
//        Merch merch = merchService.getMerchWithAuth(id);
//        if (merch == null) {
//            model.addAttribute("errorMessage", String.format(ApiInfo.GET_DATA_FAILED_404, Entity.MERCH.getNameZh()));
//            return "/error/404";
//        }
//        model.addAttribute("merch", merchVOMapper.merch2VO(merch));
//        //前端选项数据
//        model.addAttribute("options", entityUtil.getDetailOptions(Entity.MERCH.getId()));
//        //实体类通用信息
//        model.addAttribute("detailInfo", entityUtil.getItemDetailInfo(merch, Entity.MERCH.getId()));
//        //获取页面数据
//        model.addAttribute("pageInfo", entityService.getPageInfo(Entity.MERCH.getId(), id, merch));
//        //图片相关
//        model.addAttribute("itemImageInfo", CommonImageUtil.segmentImages(merch.getImages(), 200, Entity.MERCH, false));
//        //获取相关周边
//        // model.addAttribute("relatedMerchs", merchService.getRelatedMerchs(id));
//        return "/database/itemDetail/merch-detail";
//    }

    //endregion

    //region ------增删改查------

    //新增周边
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addMerch(@RequestBody String json) {
        ApiResult res = new ApiResult();
        JSONObject param = JSON.parseObject(json);
        try {
            //检测数据
            String errorMsg = merchService.checkMerchJson(param);
            if(!StringUtils.isBlank(errorMsg)) {
                res.setErrorMessage(errorMsg);
                return res.toJson();
            }

            Merch merch = entityService.json2Entity(merchService.handleMerchJson(param), Merch.class);

            //保存新增周边
//            res.message = merchService.addMerch(merch);
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return res.toJson();
    }

    //删除周边(单个/多个)
//    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
//    @ResponseBody
//    public String deleteMerch(@RequestBody String json) {
//        ApiResult res = new ApiResult();
//        try {
//            List<Merch> merchs = JSON.parseArray(json).toJavaList(Merch.class);
//            for (Merch merch : merchs) {
//                //从数据库中删除专辑
//                merchService.deleteMerch(merch);
//            }
//            res.message = String.format(ApiInfo.DELETE_DATA_SUCCESS, Entity.MERCH.getNameZh());
//        } catch (Exception ex) {
//            res.setErrorMessage(ex.getMessage());
//        }
//        return res.toJson();
//    }

    //更新周边基础信息
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String updateMerch(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            JSONObject param = JSON.parseObject(json);
            //检测数据
            String errorMsg = merchService.checkMerchJson(param);
            if(!StringUtils.isBlank(errorMsg)) {
                res.setErrorMessage(errorMsg);
                return res.toJson();
            }

            Merch merch = entityService.json2Entity(merchService.handleMerchJson(param), Merch.class);

            //修改编辑时间
            merch.setEditedTime(DateUtil.NOW_TIMESTAMP);

//            res.message = merchService.updateMerch(merch.getId(), merch);
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return res.toJson();
    }

    //endregion

    //region ------进阶信息增删改查------

    //根据搜索条件获取周边--列表界面
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/get-merchs", method = RequestMethod.POST)
    @ResponseBody
    public String getMerchsByFilterList(@RequestBody String json, HttpServletRequest request) {
        JSONObject param = JSON.parseObject(json);
        QueryParams queryParam = JSON.to(QueryParams.class, param.getJSONObject("queryParams"));
        String pageLabel = param.getString("pageLabel");

        List<MerchVOAlpha> merchs = new ArrayList<>();

        SearchResult searchResult = merchService.getMerchsByFilterList(queryParam);

        if (StringUtils.equals(pageLabel, "list")) {
            merchs = merchVOMapper.merch2VOAlpha((List<Merch>) searchResult.data);
        }
        if (StringUtils.equals(pageLabel, "index")) {
            merchs = merchVOMapper.merch2VOAlpha((List<Merch>) searchResult.data);
        }

        JSONObject result = new JSONObject();
        result.put("data", merchs);
        result.put("total", searchResult.total);

        return JSON.toJSONString(result);
    }

    @RequestMapping(value = "/get-related-merchs", method = RequestMethod.POST)
    @ResponseBody
    public String getRelatedMerchs(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {

            JSONObject param = JSON.parseObject(json);
            int id = param.getInteger("id");
            res.data = merchService.getRelatedMerchs(id);

        }catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

    //endregion

}
