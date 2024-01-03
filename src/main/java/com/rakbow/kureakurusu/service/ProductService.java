package com.rakbow.kureakurusu.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.controller.interceptor.AuthorityInterceptor;
import com.rakbow.kureakurusu.dao.ProductMapper;
import com.rakbow.kureakurusu.data.ApiInfo;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.vo.product.ProductVOAlpha;
import com.rakbow.kureakurusu.entity.Product;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.DataFinder;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.common.RedisUtil;
import com.rakbow.kureakurusu.util.convertMapper.entity.ProductVOMapper;
import com.rakbow.kureakurusu.util.entity.ProductUtil;
import com.rakbow.kureakurusu.util.file.QiniuFileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Rakbow
 * @since 2022-08-20 2:02
 */
@Service
public class ProductService {

    //region ------注入实例------
    @Resource
    private ProductMapper productMapper;
    @Resource
    private QiniuFileUtil qiniuFileUtil;
    @Resource
    private RedisUtil redisUtil;

    

    private final ProductVOMapper productVOMapper = ProductVOMapper.INSTANCES;
    //endregion

    //region ------基础增删改查------

    //新增作品
//    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
//    public String addProduct(Product product) {
//        int id = productMapper.addProduct(product);
//        return String.format(ApiInfo.INSERT_DATA_SUCCESS, Entity.PRODUCT.getNameZh());
//    }

    //通过id查找作品
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public Product getProduct(int id) {
        return productMapper.getProduct(id, true);
    }

    /**
     * 根据Id获取Product,需要判断权限
     *
     * @param id id
     * @return Product
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public Product getProductWithAuth(int id) {
        if(AuthorityInterceptor.isSenior()) {
            return productMapper.getProduct(id, true);
        }
        return productMapper.getProduct(id, false);
    }

    //更新作品信息
//    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
//    public String updateProduct(int id, Product product) {
//        productMapper.updateProduct(id, product);
//        return String.format(ApiInfo.UPDATE_DATA_SUCCESS, Entity.PRODUCT.getNameZh());
//    }

    //删除作品
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public int deleteProduct(int id) {
        deleteAllProductImages(id);
        return productMapper.deleteProduct(id);
    }

    //通过系列id获取所有作品的数组，供前端选项用
    public JSONArray getProductSet(List<Integer> franchises, int entityType, String lang) {

        JSONArray productSet = new JSONArray();

        if (franchises.size() != 0) {
            List<Integer> categories = ProductUtil.getCategoriesByEntityType(entityType);
            List<Product> products = productMapper.getProductsByFilter(null, null, franchises, categories,
                    false, null, -1, 0, 0);
            products.forEach(product -> {
                JSONObject jo = new JSONObject();
                jo.put("value", product.getId());
                if(StringUtils.equals(lang, Locale.CHINESE.getLanguage())) {
//                    jo.put("label", product.getNameZh() + "(" + ProductCategory.getNameById(product.getCategory(), lang) + ")");
                }
                if(StringUtils.equals(lang, Locale.ENGLISH.getLanguage())) {
//                    jo.put("label", product.getNameEn() + "(" + ProductCategory.getNameById(product.getCategory(), lang) + ")");
                }

                productSet.add(jo);
            });
        }
        return productSet;
    }

    //endregion

    //region ------数据处理------

    /**
     * 检测数据合法性
     *
     * @param productJson json
     * @return string类型错误消息，若为空则数据检测通过
     * @author rakbow
     */
    public String checkProductJson(JSONObject productJson) {
        if (StringUtils.isBlank(productJson.getString("name"))) {
            return I18nHelper.getMessage("entity.crud.name.required_field");
        }
        if (StringUtils.isBlank(productJson.getString("nameZh"))) {
            return I18nHelper.getMessage("entity.crud.name_zh.required_field");
        }
        if (StringUtils.isBlank(productJson.getString("releaseDate"))) {
            return I18nHelper.getMessage("entity.crud.release_date.required_field");
        }
        if (StringUtils.isBlank(productJson.getString("franchise"))) {
            return I18nHelper.getMessage("entity.crud.franchise.required_field");
        }
        if (StringUtils.isBlank(productJson.getString("category"))) {
            return I18nHelper.getMessage("entity.crud.category.required_field");
        }
        return "";
    }

    /**
     * 更新关联组织信息
     *
     * @param id            id
     * @param organizations 关联组织json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String updateProductOrganizations(int id, String organizations) {
        productMapper.updateProductOrganizations(id, organizations, DateHelper.NOW_TIMESTAMP);
        return I18nHelper.getMessage("entity.crud.companies.update.success");
    }

    /**
     * 更新Staff
     *
     * @param id     作品id
     * @param staffs staff相关信息json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String updateProductStaffs(int id, String staffs) {
        productMapper.updateProductStaffs(id, staffs, DateHelper.NOW_TIMESTAMP);
        return I18nHelper.getMessage("entity.crud.personnel.update.success");
    }

    /**
     * 删除该作品所有图片
     *
     * @param id 作品id
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void deleteAllProductImages(int id) {
        Product product = getProduct(id);
        qiniuFileUtil.commonDeleteAllFiles(JSON.parseArray(product.getImages()));

    }

    /**
     * 获取相关作品
     *
     * @param productId 作品id
     * @return list
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<ProductVOAlpha> getRelatedProducts(int productId) {
        List<ProductVOAlpha> relatedProducts;

        Product product = getProduct(productId);

        List<Integer> franchises = new ArrayList<>();
        franchises.add(product.getFranchise());

        List<Integer> categories = new ArrayList<>();
        categories.add(product.getCategory());

        List<Product> sameFranchiseProducts = productMapper.getProductsByFilter
                (null, null, franchises, categories, false, null, -1, 0, 0);
        List<Product> products = DataFinder.findProductsByClassification(product.getCategory(), sameFranchiseProducts);

        products.removeIf(it -> it.getId() == productId);

        relatedProducts = productVOMapper.product2VOAlpha(products);

        if (relatedProducts.size() > 5) {
            relatedProducts = relatedProducts.subList(0, 5);
        }

        return relatedProducts;
    }

    //endregion

    //region ------特殊查询------

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public SearchResult getProductsByFilter(QueryParams param) {

        JSONObject filter = param.getFilters();

        String name = filter.getJSONObject("name").getString("value");
        String nameZh = filter.getJSONObject("nameZh").getString("value");
        List<Integer> franchises = filter.getJSONObject("franchise").getList("value", Integer.class);
        List<Integer> categories = filter.getJSONObject("category").getList("value", Integer.class);

        List<Product> products  = new ArrayList<>();
        // List<Product> products = productMapper.getProductsByFilter(name, nameZh, franchises, categories, AuthorityInterceptor.isSenior(),
        //         param.getSortField(), param.getSortOrder(), param.getFirst(), param.getRows());
        int total = productMapper.getProductsRowsByFilter(name, nameZh, franchises, categories, AuthorityInterceptor.isSenior());

        return new SearchResult(products, total);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<ProductVOAlpha> getProductsByFranchiseId(int franchiseId) {

        List<Integer> franchise = new ArrayList<>();
        franchise.add(franchiseId);

        List<Product> products = productMapper.getProductsByFilter(null, null, franchise,
                null, false, "releaseDate", 1, 0, 0);

        return productVOMapper.product2VOAlpha(products);
    }

    //endregion

    //region ------图片操作------
    //endregion

}
