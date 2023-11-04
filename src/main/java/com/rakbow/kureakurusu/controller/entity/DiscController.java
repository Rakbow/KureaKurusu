package com.rakbow.kureakurusu.controller.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.vo.disc.DiscVOAlpha;
import com.rakbow.kureakurusu.entity.Disc;
import com.rakbow.kureakurusu.service.DiscService;
import com.rakbow.kureakurusu.service.EntityService;
import com.rakbow.kureakurusu.service.I18nService;
import com.rakbow.kureakurusu.service.UserService;
import com.rakbow.kureakurusu.data.ApiInfo;
import com.rakbow.kureakurusu.data.ApiResult;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.common.EntityUtil;
import com.rakbow.kureakurusu.util.convertMapper.entity.DiscVOMapper;
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
 * @Create: 2022-12-15 20:57
 * @Description:
 */
@Controller
@RequestMapping("/db/disc")
public class DiscController {

    //region ------引入实例------

    private static final Logger logger = LoggerFactory.getLogger(DiscController.class);

    @Resource
    private DiscService discService;
    @Resource
    private UserService userService;
    @Resource
    private EntityUtil entityUtil;
    @Resource
    private EntityService entityService;
    @Resource
    private I18nService i18n;

    private final DiscVOMapper discVOMapper = DiscVOMapper.INSTANCES;

    //endregion

    //region ------获取页面------

    //获取单个专辑详细信息页面
    // @UniqueVisitor
    // @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    // public String getAlbumDetail(@PathVariable("id") int id, Model model) {
    //     Disc disc = discService.getDiscWithAuth(id);
    //     if (discService.getDisc(id) == null) {
    //         model.addAttribute("errorMessage", String.format(ApiInfo.GET_DATA_FAILED_404, Entity.DISC.getNameZh()));
    //         return "/error/404";
    //     }
    //     model.addAttribute("disc", discVOMapper.disc2VO(disc));
    //     //前端选项数据
    //     model.addAttribute("options", entityUtil.getDetailOptions(Entity.DISC.getId()));
    //     //实体类通用信息
    //     model.addAttribute("detailInfo", entityUtil.getItemDetailInfo(disc, Entity.DISC.getId()));
    //     //获取页面数据
    //     model.addAttribute("pageInfo", entityService.getPageInfo(Entity.DISC.getId(), id, disc));
    //     //图片相关
    //     model.addAttribute("itemImageInfo", CommonImageUtil.segmentImages(disc.getImages(), 200, Entity.DISC, false));
    //     //获取相关碟片
    //     // model.addAttribute("relatedDiscs", discService.getRelatedDiscs(id));
    //     return "/database/itemDetail/disc-detail";
    // }

    //endregion

    //region ------增删改查------

    //根据搜索条件获取碟片--列表界面
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/get-discs", method = RequestMethod.POST)
    @ResponseBody
    public String getDiscsByFilterList(@RequestBody String json, HttpServletRequest request) {
        JSONObject param = JSON.parseObject(json);
        QueryParams queryParam = JSON.to(QueryParams.class, param.getJSONObject("queryParams"));
        String pageLabel = param.getString("pageLabel");

        List<DiscVOAlpha> discs = new ArrayList<>();

        SearchResult searchResult = discService.getDiscsByFilterList(queryParam);

        if (StringUtils.equals(pageLabel, "list")) {
            discs = discVOMapper.disc2VOAlpha((List<Disc>) searchResult.data);
        }
        if (StringUtils.equals(pageLabel, "index")) {
            discs = discVOMapper.disc2VOAlpha((List<Disc>) searchResult.data);
        }

        JSONObject result = new JSONObject();
        result.put("data", discs);
        result.put("total", searchResult.total);

        return JSON.toJSONString(result);
    }

    //新增碟片
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDisc(@RequestBody String json) {
        ApiResult res = new ApiResult();
        JSONObject param = JSON.parseObject(json);
        try {
            //检测数据
            String errorMsg = discService.checkDiscJson(param);
            if(!StringUtils.isBlank(errorMsg)) {
                res.setErrorMessage(errorMsg);
                return res.toJson();
            }

            Disc disc = entityService.json2Entity(discService.handleDiscJson(param), Disc.class);

            //保存新增专辑

            res.message = discService.addDisc(disc);
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return res.toJson();
    }

    //删除碟片(单个/多个)
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteDisc(@RequestBody String json) {
        ApiResult res = new ApiResult();
        List<Disc> discs = JSON.parseArray(json).toJavaList(Disc.class);
        try {
            for (Disc disc : discs) {
                //从数据库中删除专辑
                discService.deleteDisc(disc);
            }
            res.message = i18n.getMessage("entity.curd.delete.success", Entity.DISC.getNameZh());
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return res.toJson();
    }

    //更新碟片基础信息
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String updateDisc(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            JSONObject param = JSON.parseObject(json);
            //检测数据
            String errorMsg = discService.checkDiscJson(param);
            if(!StringUtils.isBlank(errorMsg)) {
                res.setErrorMessage(errorMsg);
                return res.toJson();
            }

            Disc disc = entityService.json2Entity(discService.handleDiscJson(param), Disc.class);

            //修改编辑时间
            disc.setEditedTime(DateHelper.NOW_TIMESTAMP);

            res.message = discService.updateDisc(disc.getId(), disc);
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return res.toJson();
    }

    //endregion

    //region ------进阶信息增删改查------

    @RequestMapping(value = "/get-related-discs", method = RequestMethod.POST)
    @ResponseBody
    public String getRelatedDiscs(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {

            JSONObject param = JSON.parseObject(json);
            int id = param.getInteger("id");
            res.data = discService.getRelatedDiscs(id);

        }catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

    //endregion

}
