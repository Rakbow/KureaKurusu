package com.rakbow.kureakurusu.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.controller.interceptor.AuthorityInterceptor;
import com.rakbow.kureakurusu.dao.DiscMapper;
import com.rakbow.kureakurusu.data.ApiInfo;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.vo.disc.DiscVOBeta;
import com.rakbow.kureakurusu.entity.Disc;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.CommonUtil;
import com.rakbow.kureakurusu.util.common.VisitUtil;
import com.rakbow.kureakurusu.util.convertMapper.entity.DiscVOMapper;
import com.rakbow.kureakurusu.util.file.QiniuFileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Rakbow
 * @since 2022-12-14 23:24 disc业务层
 */
@Service
public class DiscService {

    //region ------注入依赖------

    @Resource
    private DiscMapper discMapper;
    @Resource
    private QiniuFileUtil qiniuFileUtil;
    @Resource
    private VisitUtil visitUtil;
    

    private final DiscVOMapper discVOMapper = DiscVOMapper.INSTANCES;

    //endregion

    //region ------更删改查------

    /**
     * 新增碟片
     *
     * @param disc 新增的碟片
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String addDisc(Disc disc) {
        int id = discMapper.addDisc(disc);
        return I18nHelper.getMessage("entity.curd.insert.success", Entity.DISC.getNameZh());
    }

    /**
     * 根据Id获取碟片
     *
     * @param id 碟片id
     * @return disc
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public Disc getDisc(int id) {
        return discMapper.getDisc(id, true);
    }

    /**
     * 根据Id获取需要判断权限
     *
     * @param id id
     * @return Disc
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public Disc getDiscWithAuth(int id) {
        if(AuthorityInterceptor.isSenior()) {
            return discMapper.getDisc(id, true);
        }
        return discMapper.getDisc(id, false);
    }

    /**
     * 根据Id删除碟片
     *
     * @param disc 碟片
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void deleteDisc(Disc disc) {
        //删除前先把服务器上对应图片全部删除
        qiniuFileUtil.commonDeleteAllFiles(JSON.parseArray(disc.getImages()));
        discMapper.deleteDisc(disc.getId());
        visitUtil.deleteVisit(Entity.DISC.getId(), disc.getId());
    }

    /**
     * 更新碟片基础信息
     *
     * @param id 碟片id
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String updateDisc(int id, Disc disc) {
        discMapper.updateDisc(id, disc);
        return I18nHelper.getMessage("entity.curd.update.success", Entity.DISC.getNameZh());
    }

    //endregion

    //region ------数据处理------

    /**
     * 检测数据合法性
     *
     * @param discJson discJson
     * @return string类型错误消息，若为空则数据检测通过
     * @author rakbow
     */
    public String checkDiscJson(JSONObject discJson) {
        if (StringUtils.isBlank(discJson.getString("name"))) {
            return I18nHelper.getMessage("entity.crud.name.required_field");
        }
        if (StringUtils.isBlank(discJson.getString("releaseDate"))) {
            return I18nHelper.getMessage("entity.crud.release_date.required_field");
        }
        if (StringUtils.isBlank(discJson.getString("franchises"))
                || StringUtils.equals(discJson.getString("franchises"), "[]")) {
            return I18nHelper.getMessage("entity.crud.category.required_field");
        }
        if (StringUtils.isBlank(discJson.getString("products"))
                || StringUtils.equals(discJson.getString("products"), "[]")) {
            return I18nHelper.getMessage("entity.crud.product.required_field");
        }
        if (StringUtils.isBlank(discJson.getString("mediaFormat"))
                || StringUtils.equals(discJson.getString("mediaFormat"), "[]")) {
            return I18nHelper.getMessage("entity.crud.media_format.required_field");
        }
        if (StringUtils.isBlank(discJson.getString("region"))) {
            return I18nHelper.getMessage("entity.crud.region.required_field");
        }
        return "";
    }

    /**
     * 处理前端传送碟片数据
     *
     * @param discJson discJson
     * @return 处理后的disc json格式数据
     * @author rakbow
     */
    public JSONObject handleDiscJson(JSONObject discJson) {

        String[] products = CommonUtil.str2SortedArray(discJson.getString("products"));
        String[] franchises = CommonUtil.str2SortedArray(discJson.getString("franchises"));
        String[] mediaFormat = CommonUtil.str2SortedArray(discJson.getString("mediaFormat"));

        discJson.put("releaseDate", discJson.getDate("releaseDate"));
        discJson.put("products", "{\"ids\":[" + StringUtils.join(products, ",") + "]}");
        discJson.put("franchises", "{\"ids\":[" + StringUtils.join(franchises, ",") + "]}");
        discJson.put("mediaFormat", "{\"ids\":[" + StringUtils.join(mediaFormat, ",") + "]}");

        return discJson;
    }

    //endregion

    //region ------更新数据------

    //endregion

    //region ------特殊查询------

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public SearchResult getDiscsByFilterList (QueryParams param) {

        // JSONObject filter = param.getFilters();
        //
        // String catalogNo = filter.getJSONObject("catalogNo").getString("value");
        // String name = filter.getJSONObject("name").getString("value");
        // String region = filter.getJSONObject("region").getString("value");
        //
        // List<Integer> franchises = filter.getJSONObject("franchises").getList("value", Integer.class);
        // List<Integer> products = filter.getJSONObject("products").getList("value", Integer.class);
        // List<Integer> mediaFormat = filter.getJSONObject("mediaFormat").getList("value", Integer.class);
        //
        // String limited;
        // if (filter.getJSONObject("limited").get("value") == null) {
        //     limited = null;
        // }else {
        //     limited = filter.getJSONObject("limited").getBoolean("value")
        //             ?Integer.toString(1):Integer.toString(0);
        // }
        //
        // String hasBonus;
        // if (filter.getJSONObject("hasBonus").get("value") == null) {
        //     hasBonus = null;
        // }else {
        //     hasBonus = filter.getJSONObject("hasBonus").getBoolean("value")
        //             ?Integer.toString(1):Integer.toString(0);
        // }
        //
        // List<Disc> discs = new ArrayList<>();
        //
        // // List<Disc> discs = discMapper.getDiscsByFilter(catalogNo, name, region, franchises, products,
        // //         mediaFormat, limited, hasBonus, AuthorityInterceptor.isSenior(), param.getSortField(), param.getSortOrder(),  param.getFirst(), param.getRows());
        //
        // int total = discMapper.getDiscsRowsByFilter(catalogNo, name, region, franchises, products,
        //         mediaFormat, limited, hasBonus, AuthorityInterceptor.isSenior());
        //
        // return new SearchResult(discs, total);
        return null;
    }

    /**
     * 获取相关联Disc
     *
     * @param id 碟片id
     * @return list封装的Disc
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<DiscVOBeta> getRelatedDiscs(int id) {
        List<Disc> result = new ArrayList<>();

        Disc disc = getDisc(id);

        //该Disc包含的作品id
        List<Integer> productIds = JSONObject.parseObject(disc.getProducts()).getList("ids", Integer.class);

        //该系列所有Disc
        List<Disc> allDiscs = discMapper.getDiscsByFilter(null, null, null,
                        CommonUtil.ids2List(disc.getFranchises()), null, null, null,
                        null, false, "releaseDate", 1, 0, 0)
                .stream().filter(tmpDisc -> tmpDisc.getId() != disc.getId()).collect(Collectors.toList());

        List<Disc> queryResult = allDiscs.stream().filter(tmpDisc ->
                StringUtils.equals(tmpDisc.getProducts(), disc.getProducts())).collect(Collectors.toList());

        if (queryResult.size() > 5) {//结果大于5
            result.addAll(queryResult.subList(0, 5));
        } else if (queryResult.size() == 5) {//结果等于5
            result.addAll(queryResult);
        } else if (queryResult.size() > 0) {//结果小于5不为空
            List<Disc> tmp = new ArrayList<>(queryResult);

            if (productIds.size() > 1) {
                List<Disc> tmpQueryResult = allDiscs.stream().filter(tmpDisc ->
                        JSONObject.parseObject(tmpDisc.getProducts()).getList("ids", Integer.class)
                                .contains(productIds.get(1))).collect(Collectors.toList());

                if (tmpQueryResult.size() >= 5 - queryResult.size()) {
                    tmp.addAll(tmpQueryResult.subList(0, 5 - queryResult.size()));
                } else if (tmpQueryResult.size() > 0 && tmpQueryResult.size() < 5 - queryResult.size()) {
                    tmp.addAll(tmpQueryResult);
                }
            }
            result.addAll(tmp);
        } else {
            List<Disc> tmp = new ArrayList<>(queryResult);
            for (int productId : productIds) {
                tmp.addAll(
                        allDiscs.stream().filter(tmpDisc ->
                                JSONObject.parseObject(tmpDisc.getProducts()).getList("ids", Integer.class)
                                        .contains(productId)).collect(Collectors.toList())
                );
            }
            result = CommonUtil.removeDuplicateList(tmp);
            if (result.size() >= 5) {
                result = result.subList(0, 5);
            }
        }

        return discVOMapper.disc2VOBeta(CommonUtil.removeDuplicateList(result));
    }

    /**
     * 根据作品id获取关联disc
     *
     * @param productId 作品id
     * @return List<JSONObject>
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<DiscVOBeta> getDiscsByProductId(int productId) {
        List<Integer> products = new ArrayList<>();
        products.add(productId);

        List<Disc> discs = discMapper.getDiscsByFilter(null, null, null, null, products,
                null, null, null, false, "releaseDate", -1,  0, 0);

        return discVOMapper.disc2VOBeta(discs);
    }

    //endregion

}
