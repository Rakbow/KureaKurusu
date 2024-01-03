package com.rakbow.kureakurusu.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.controller.interceptor.AuthorityInterceptor;
import com.rakbow.kureakurusu.dao.MerchMapper;
import com.rakbow.kureakurusu.data.ApiInfo;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.vo.merch.MerchVOBeta;
import com.rakbow.kureakurusu.entity.Merch;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.CommonUtil;
import com.rakbow.kureakurusu.util.common.VisitUtil;
import com.rakbow.kureakurusu.util.convertMapper.entity.MerchVOMapper;
import com.rakbow.kureakurusu.util.file.QiniuFileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Rakbow
 * @since 2023-01-04 14:19 merch业务层
 */
@Service
public class MerchService {

    //region ------引入实例------

    @Resource
    private MerchMapper merchMapper;
    @Resource
    private QiniuFileUtil qiniuFileUtil;
    @Resource
    private VisitUtil visitUtil;

    private final MerchVOMapper merchVOMapper = MerchVOMapper.INSTANCES;
    

    //endregion

    //region ------更删改查------

//    /**
//     * 新增周边
//     *
//     * @param merch 新增的周边
//     * @author rakbow
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
//    public String addMerch(Merch merch) {
//        int id = merchMapper.addMerch(merch);
//        return String.format(ApiInfo.INSERT_DATA_SUCCESS, Entity.MERCH.getNameZh());
//    }

    /**
     * 根据Id获取周边
     *
     * @param id 周边id
     * @return merch
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public Merch getMerch(int id) {
        return merchMapper.getMerch(id, true);
    }

    /**
     * 根据Id获取Merch,需要判断权限
     *
     * @param id id
     * @return Merch
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public Merch getMerchWithAuth(int id) {
        if(AuthorityInterceptor.isSenior()) {
            return merchMapper.getMerch(id, true);
        }
        return merchMapper.getMerch(id, false);
    }

//    /**
//     * 根据Id删除周边
//     *
//     * @param merch 周边
//     * @author rakbow
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
//    public void deleteMerch(Merch merch) {
//        //删除前先把服务器上对应图片全部删除
//        qiniuFileUtil.commonDeleteAllFiles(JSON.parseArray(merch.getImages()));
//        merchMapper.deleteMerch(merch.getId());
//        visitUtil.deleteVisit(Entity.MERCH.getId(), merch.getId());
//    }

//    /**
//     * 更新周边基础信息
//     *
//     * @param id 周边id
//     * @author rakbow
//     */
//    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
//    public String updateMerch(int id, Merch merch) {
//        merchMapper.updateMerch(id, merch);
//        return String.format(ApiInfo.UPDATE_DATA_SUCCESS, Entity.MERCH.getNameZh());
//    }

    //endregion

    //region ------数据处理------

    /**
     * 检测数据合法性
     *
     * @param merchJson merchJson
     * @return string类型错误消息，若为空则数据检测通过
     * @author rakbow
     */
    public String checkMerchJson(JSONObject merchJson) {
        if (StringUtils.isBlank(merchJson.getString("name"))) {
            return I18nHelper.getMessage("entity.crud.name.required_field");
        }
        if (StringUtils.isBlank(merchJson.getString("releaseDate"))) {
            return I18nHelper.getMessage("entity.crud.release_date.required_field");
        }
        if (StringUtils.isBlank(merchJson.getString("category"))) {
            return I18nHelper.getMessage("entity.crud.category.required_field");
        }
        if (StringUtils.isBlank(merchJson.getString("franchises"))
                || StringUtils.equals(merchJson.getString("franchises"), "[]")) {
            return I18nHelper.getMessage("entity.crud.franchise.required_field");
        }
        if (StringUtils.isBlank(merchJson.getString("products"))
                || StringUtils.equals(merchJson.getString("products"), "[]")) {
            return I18nHelper.getMessage("entity.crud.product.required_field");
        }
        return "";
    }

    /**
     * 处理前端传送周边数据
     *
     * @param merchJson merchJson
     * @return 处理后的merch json格式数据
     * @author rakbow
     */
    public JSONObject handleMerchJson(JSONObject merchJson) {

        String[] products = CommonUtil.str2SortedArray(merchJson.getString("products"));
        String[] franchises = CommonUtil.str2SortedArray(merchJson.getString("franchises"));

        merchJson.put("releaseDate", merchJson.getDate("releaseDate"));
        merchJson.put("products", "{\"ids\":[" + StringUtils.join(products, ",") + "]}");
        merchJson.put("franchises", "{\"ids\":[" + StringUtils.join(franchises, ",") + "]}");

        return merchJson;
    }

    //endregion

    //region ------更新merch数据------

    //endregion

    //region ------特殊查询------

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public SearchResult getMerchsByFilterList(QueryParams param) {

        // JSONObject filter = param.getFilters();
        //
        // String name = filter.getJSONObject("name").getString("value");
        // String barcode = filter.getJSONObject("barcode").getString("value");
        // String region = filter.getJSONObject("region").getString("value");
        //
        // int category = 100;
        // if (filter.getJSONObject("category").get("value") != null) {
        //     category = filter.getJSONObject("category").getIntValue("value");
        // }
        //
        // List<Integer> franchises = filter.getJSONObject("franchises").getList("value", Integer.class);
        // List<Integer> products = filter.getJSONObject("products").getList("value", Integer.class);
        //
        // String notForSale;
        // if (filter.getJSONObject("notForSale").get("value") == null) {
        //     notForSale = null;
        // } else {
        //     notForSale = filter.getJSONObject("notForSale").getBoolean("value")
        //             ? Integer.toString(1) : Integer.toString(0);
        // }
        //
        // List<Merch> merchs  = new ArrayList<>();
        //
        // // List<Merch> merchs = merchMapper.getMerchsByFilter(name, barcode, franchises, products, category, region,
        // //         notForSale, AuthorityInterceptor.isSenior(), param.getSortField(), param.getSortOrder(), param.getFirst(), param.getRows());
        //
        // int total = merchMapper.getMerchsRowsByFilter(name, barcode, franchises, products, category, region, notForSale, AuthorityInterceptor.isSenior());
        //
        // return new SearchResult(merchs, total);
        return null;
    }

    /**
     * 根据作品id获取关联周边
     *
     * @param productId 作品id
     * @return List<JSONObject>
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<MerchVOBeta> getMerchsByProductId(int productId) {
        List<Integer> products = new ArrayList<>();
        products.add(productId);

        List<Merch> merchs = merchMapper.getMerchsByFilter(null, null, null, products,
                100, null, null, false, "releaseDate", -1,  0, 0);

        return merchVOMapper.merch2VOBeta(merchs);
    }

    /**
     * 获取相关联Merch
     *
     * @param id 周边id
     * @return list封装的Merch
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<MerchVOBeta> getRelatedMerchs(int id) {
        List<Merch> result = new ArrayList<>();

        Merch merch = getMerch(id);

        //该Merch包含的作品id
        List<Integer> productIds = JSONObject.parseObject(merch.getProducts()).getList("ids", Integer.class);

        //该系列所有Merch
        List<Merch> allMerchs = merchMapper.getMerchsByFilter(null, null, CommonUtil.ids2List(merch.getFranchises()),
                        null, 100, null, null, false, "releaseDate", 1, 0, 0)
                .stream().filter(tmpMerch -> tmpMerch.getId() != merch.getId()).collect(Collectors.toList());

        List<Merch> queryResult = allMerchs.stream().filter(tmpMerch ->
                StringUtils.equals(tmpMerch.getProducts(), merch.getProducts())).collect(Collectors.toList());

        if (queryResult.size() > 5) {//结果大于5
            result.addAll(queryResult.subList(0, 5));
        } else if (queryResult.size() == 5) {//结果等于5
            result.addAll(queryResult);
        } else if (queryResult.size() > 0) {//结果小于5不为空
            List<Merch> tmp = new ArrayList<>(queryResult);

            if (productIds.size() > 1) {
                List<Merch> tmpQueryResult = allMerchs.stream().filter(tmpMerch ->
                        JSONObject.parseObject(tmpMerch.getProducts()).getList("ids", Integer.class)
                                .contains(productIds.get(1))).collect(Collectors.toList());

                if (tmpQueryResult.size() >= 5 - queryResult.size()) {
                    tmp.addAll(tmpQueryResult.subList(0, 5 - queryResult.size()));
                } else if (tmpQueryResult.size() > 0 && tmpQueryResult.size() < 5 - queryResult.size()) {
                    tmp.addAll(tmpQueryResult);
                }
            }
            result.addAll(tmp);
        } else {
            List<Merch> tmp = new ArrayList<>(queryResult);
            for (int productId : productIds) {
                tmp.addAll(
                        allMerchs.stream().filter(tmpMerch ->
                                JSONObject.parseObject(tmpMerch.getProducts()).getList("ids", Integer.class)
                                        .contains(productId)).collect(Collectors.toList())
                );
            }
            result = CommonUtil.removeDuplicateList(tmp);
            if (result.size() >= 5) {
                result = result.subList(0, 5);
            }
        }

        return merchVOMapper.merch2VOBeta(CommonUtil.removeDuplicateList(result));
    }

    //endregion

}
