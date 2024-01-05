package com.rakbow.kureakurusu.controller.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.vo.game.GameVOAlpha;
import com.rakbow.kureakurusu.entity.Game;
import com.rakbow.kureakurusu.service.*;
import com.rakbow.kureakurusu.data.ApiInfo;
import com.rakbow.kureakurusu.data.ApiResult;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.common.EntityUtil;
import com.rakbow.kureakurusu.util.convertMapper.entity.GameVOMapper;
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
 * @author Rakbow
 * @since 2023-01-06 15:50
 */

@Controller
@RequestMapping("/db/game")
public class GameController {

    //region ------引入实例------

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    @Resource
    private GameService gameService;
    @Resource
    private UserService userService;
    @Resource
    private EntityUtil entityUtil;
    @Resource
    private EntityService entityService;
    

    private final GameVOMapper gameVOMapper = GameVOMapper.INSTANCES;

    //endregion

    //region ------获取页面------

    //获取单个游戏详细信息页面
    // @UniqueVisitor
    // @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    // public String getGameDetail(@PathVariable("id") Integer id, Model model) {
    //     Game game = gameService.getGameWithAuth(id);
    //     if (game == null) {
    //         model.addAttribute("errorMessage", String.format(ApiInfo.GET_DATA_FAILED_404, Entity.GAME.getNameZh()));
    //         return "/error/404";
    //     }
    //     model.addAttribute("game", gameVOMapper.game2VO(game));
    //     //前端选项数据
    //     model.addAttribute("options", entityUtil.getDetailOptions(Entity.GAME.getId()));
    //     //实体类通用信息
    //     model.addAttribute("detailInfo", entityUtil.getItemDetailInfo(game, Entity.GAME.getId()));
    //     //获取页面数据
    //     model.addAttribute("pageInfo", entityService.getPageInfo(Entity.GAME.getId(), id, game));
    //     //图片相关
    //     model.addAttribute("itemImageInfo", CommonImageUtil.segmentImages(game.getImages(), 140, Entity.GAME, false));
    //     //获取相关游戏
    //     // model.addAttribute("relatedGames", gameService.getRelatedGames(id));
    //     return "/database/itemDetail/game-detail";
    // }

    //endregion

    //region ------增删改查------

    //新增游戏
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addGame(@RequestBody String json) {
        ApiResult res = new ApiResult();
        JSONObject param = JSON.parseObject(json);
        try {
            //检测数据
            String errorMsg = gameService.checkGameJson(param);
            if(!StringUtils.isBlank(errorMsg)) {
                res.setErrorMessage(errorMsg);
                return res.toJson();
            }

            Game game = entityService.json2Entity(gameService.handleGameJson(param), Game.class);

            //保存新增游戏
            res.message = gameService.addGame(game);
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return res.toJson();
    }

    //删除游戏(单个/多个)
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteGame(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            List<Game> games = JSON.parseArray(json).toJavaList(Game.class);
            for (Game game : games) {
                //从数据库中删除专辑
                gameService.deleteGame(game);
            }
            res.message = I18nHelper.getMessage("entity.curd.delete.success", Entity.GAME.getName());
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return res.toJson();
    }

    //更新游戏基础信息
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String updateGame(@RequestBody String json) {
        ApiResult res = new ApiResult();
        JSONObject param = JSON.parseObject(json);
        try {
            //检测数据
            String errorMsg = gameService.checkGameJson(param);
            if(!StringUtils.isBlank(errorMsg)) {
                res.setErrorMessage(errorMsg);
                return res.toJson();
            }

            Game game = entityService.json2Entity(gameService.handleGameJson(param), Game.class);

            //修改编辑时间
            game.setEditedTime(DateHelper.now());

            res.message = gameService.updateGame(game.getId(), game);
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return res.toJson();
    }

    //endregion

    //region ------进阶信息增删改查------

    //根据搜索条件获取游戏--列表界面
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/get-games", method = RequestMethod.POST)
    @ResponseBody
    public String getGamesByFilterList(@RequestBody String json, HttpServletRequest request) {
        JSONObject param = JSON.parseObject(json);
        QueryParams queryParam = JSON.to(QueryParams.class, param.getJSONObject("queryParams"));
        String pageLabel = param.getString("pageLabel");

        List<GameVOAlpha> games = new ArrayList<>();

        SearchResult serchResult = gameService.getGamesByFilter(queryParam);

        if (StringUtils.equals(pageLabel, "list")) {
            games = gameVOMapper.game2VOAlpha((List<Game>) serchResult.data);
        }
        if (StringUtils.equals(pageLabel, "index")) {
            games = gameVOMapper.game2VOAlpha((List<Game>) serchResult.data);
        }

        JSONObject result = new JSONObject();
        result.put("data", games);
        result.put("total", serchResult.total);

        return JSON.toJSONString(result);
    }

    //更新游戏作者信息
    @RequestMapping(path = "/update-organizations", method = RequestMethod.POST)
    @ResponseBody
    public String updateGameOrganizations(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            int id = JSON.parseObject(json).getInteger("id");
            String organizations = JSON.parseObject(json).getJSONArray("organizations").toString();

            res.message = gameService.updateGameOrganizations(id, organizations);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

    //更新游戏规格信息
    @RequestMapping(path = "/update-staffs", method = RequestMethod.POST)
    @ResponseBody
    public String updateGameStaffs(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            int id = JSON.parseObject(json).getInteger("id");
            String staffs = JSON.parseObject(json).getJSONArray("staffs").toString();

            res.message = gameService.updateGameStaffs(id, staffs);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

    @RequestMapping(value = "/get-related-games", method = RequestMethod.POST)
    @ResponseBody
    public String getRelatedGames(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {

            JSONObject param = JSON.parseObject(json);
            int id = param.getInteger("id");
            res.data = gameService.getRelatedGames(id);

        }catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

    //endregion

}
