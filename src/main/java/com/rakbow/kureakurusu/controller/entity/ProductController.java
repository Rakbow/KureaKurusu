package com.rakbow.kureakurusu.controller.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.ApiResult;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.vo.product.ProductVOAlpha;
import com.rakbow.kureakurusu.data.entity.Product;
import com.rakbow.kureakurusu.service.*;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.EntityUtil;
import com.rakbow.kureakurusu.util.convertMapper.entity.ProductVOMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Rakbow
 * @since 2022-10-15 19:18
 */
@Controller
@RequestMapping(path = "/db/product")
public class ProductController {

    //region ------注入实例------

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Resource
    private ProductService productService;
    @Resource
    private UserService userService;
    @Resource
    private AlbumService albumService;
    @Resource
    private BookService bookService;
    @Resource
    private DiscService discService;
    @Resource
    private GameService gameService;
    @Resource
    private EntityUtil entityUtil;
    @Resource
    private EntityService entityService;
    

    private final ProductVOMapper productVOMapper = ProductVOMapper.INSTANCES;
    //endregion

    //region ------获取页面------

    //获取单个产品详细信息页面
//    @UniqueVisitor
//    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
//    public String getProductDetail(@PathVariable("id") int id, Model model) {
//        Product product = productService.getProductWithAuth(id);
//        if (product == null) {
//            model.addAttribute("errorMessage", String.format(ApiInfo.GET_DATA_FAILED_404, Entity.PRODUCT.getNameZh()));
//            return "/error/404";
//        }
//
//        model.addAttribute("product", productVOMapper.product2VO(product));
//        //前端选项数据
//        model.addAttribute("options", entityUtil.getDetailOptions(Entity.PRODUCT.getId()));
//        // model.addAttribute("relatedProducts", productService.getRelatedProducts(id));
//
//        if (product.getCategory() == ProductCategory.ANIMATION.getId()
//                || product.getCategory() == ProductCategory.LIVE_ACTION_MOVIE.getId()
//                || product.getCategory() == ProductCategory.OVA_OAD.getId()
//                || product.getCategory() == ProductCategory.TV_SERIES.getId()) {
//            model.addAttribute("albums", albumService.getAlbumsByProductId(id));
//            model.addAttribute("discs", discService.getDiscsByProductId(id));
//        }
//        if (product.getCategory() == ProductCategory.NOVEL.getId()
//                || product.getCategory() == ProductCategory.MANGA.getId()
//                || product.getCategory() == ProductCategory.PUBLICATION.getId()) {
//            model.addAttribute("books", bookService.getBooksByProductId(id));
//        }
//        if (product.getCategory() == ProductCategory.GAME.getId()) {
//            model.addAttribute("albums", albumService.getAlbumsByProductId(id));
//            model.addAttribute("games", gameService.getGamesByProductId(id));
//        }

//        //获取页面数据
//        model.addAttribute("pageInfo", entityService.getPageInfo(Entity.PRODUCT.getId(), id, product));
//        //实体类通用信息
//        model.addAttribute("detailInfo", EntityUtil.getMetaDetailInfo(product, Entity.PRODUCT.getId()));
//        //图片相关
//        model.addAttribute("itemImageInfo", CommonImageUtil.segmentImages(product.getImages(), 100, Entity.PRODUCT, true));
//
//        return "/database/itemDetail/product-detail";
//    }
    //endregion

    //region ------基础增删改查------

    //新增作品
//    @RequestMapping(value = "/add", method = RequestMethod.POST)
//    @ResponseBody
//    public String addProduct(@RequestBody String json) {
//        ApiResult res = new ApiResult();
//        JSONObject param = JSON.parseObject(json);
//        try {
//            //检测数据
//            String errorMsg = productService.checkProductJson(param);
//            if(!StringUtils.isBlank(errorMsg)) {
//                res.setErrorMessage(errorMsg);
//                return res.toJson();
//            }
//
//            Product product = entityService.json2Entity(param, Product.class);
//
//            //保存新增专辑
//            res.message = productService.addProduct(product);
//        } catch (Exception ex) {
//            res.setErrorMessage(ex.getMessage());
//        }
//        return res.toJson();
//    }

    //更新作品基础信息
//    @RequestMapping(value = "/update", method = RequestMethod.POST)
//    @ResponseBody
//    public String updateProduct(@RequestBody String json) {
//        ApiResult res = new ApiResult();
//        JSONObject param = JSON.parseObject(json);
//        try {
//            //检测数据
//            String errorMsg = productService.checkProductJson(param);
//            if(!StringUtils.isBlank(errorMsg)) {
//                res.setErrorMessage(errorMsg);
//                return res.toJson();
//            }
//
//            Product product = entityService.json2Entity(param, Product.class);
//
//            //修改编辑时间
//            product.setEditedTime(DateUtil.NOW_TIMESTAMP);
//
//            res.message = productService.updateProduct(product.getId(), product);
//        } catch (Exception ex) {
//            res.setErrorMessage(ex.getMessage());
//        }
//        return res.toJson();
//    }

    //更新游戏作者信息
    @RequestMapping(path = "/update-organizations", method = RequestMethod.POST)
    @ResponseBody
    public String updateProductOrganizations(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            int id = JSON.parseObject(json).getInteger("id");
            String organizations = JSON.parseObject(json).getJSONArray("organizations").toString();

            res.message = productService.updateProductOrganizations(id, organizations);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

    //更新staff
    @RequestMapping(path = "/update-staffs", method = RequestMethod.POST)
    @ResponseBody
    public String updateProductStaffs(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            int id = JSON.parseObject(json).getInteger("id");
            String staffs = JSON.parseObject(json).getJSONArray("staffs").toString();
            if (StringUtils.isBlank(staffs)) {
                res.setErrorMessage(I18nHelper.getMessage("entity.crud.input.empty"));
                return res.toJson();
            }

            res.message = productService.updateProductStaffs(id, staffs);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

    //endregion

    //region ------特殊查询------

    /**
     * 根据系列id获取该系列所有产品
     * */
    @RequestMapping(path = "/get-product-set", method = RequestMethod.POST)
    @ResponseBody
    public String getAllProductByFranchiseId(@RequestBody JSONObject json){
        ApiResult res = new ApiResult();
        try {
            List<Integer> franchiseIds = json.getList("franchises", Integer.class);
            int entity = json.getIntValue("entity");
            res.data = productService.getProductSet(franchiseIds, entity, LocaleContextHolder.getLocale().getLanguage());
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(path = "/get-products", method = RequestMethod.POST)
    @ResponseBody
    public String getProductsByFilter(@RequestBody JSONObject json){

        QueryParams queryParam = json.getObject("queryParams", QueryParams.class);

        SearchResult searchResult = productService.getProductsByFilter(queryParam);

        List<ProductVOAlpha> products = productVOMapper.product2VOAlpha((List<Product>) searchResult.data);

        JSONObject result = new JSONObject();
        result.put("data", products);
        result.put("total", searchResult.total);

        return JSON.toJSONString(result);
    }

    @RequestMapping(value = "/get-related-products", method = RequestMethod.POST)
    @ResponseBody
    public String getRelatedProducts(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {

            JSONObject param = JSON.parseObject(json);
            int id = param.getInteger("id");
            res.data = productService.getRelatedProducts(id);

        }catch (Exception e) {
            res.setErrorMessage(e);
        }
        return res.toJson();
    }

    //endregion
}
