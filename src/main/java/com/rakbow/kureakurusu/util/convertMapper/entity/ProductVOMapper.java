package com.rakbow.kureakurusu.util.convertMapper.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.CommonConstant;
import com.rakbow.kureakurusu.data.emun.entity.product.ProductCategory;
import com.rakbow.kureakurusu.data.emun.temp.EnumUtil;
import com.rakbow.kureakurusu.data.vo.product.ProductVO;
import com.rakbow.kureakurusu.data.vo.product.ProductVOAlpha;
import com.rakbow.kureakurusu.data.vo.product.ProductVOBeta;
import com.rakbow.kureakurusu.entity.Product;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.entry.EntryUtil;
import com.rakbow.kureakurusu.util.file.QiniuImageUtil;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Rakbow
 * @since 2023-01-12 10:45 Product VO转换接口
 */
@Mapper(componentModel = "spring")
public interface ProductVOMapper {

    ProductVOMapper INSTANCES = Mappers.getMapper(ProductVOMapper.class);

    /**
     * Product转VO对象，用于详情页面，转换量最大的
     *
     * @param product 作品
     * @return ProductVO
     * @author rakbow
     */
    default ProductVO product2VO(Product product) {

        ProductVO productVO = new ProductVO();

        //基础信息
        productVO.setId(product.getId());
        productVO.setName(product.getName());
        productVO.setNameEn(product.getNameEn());
        productVO.setNameZh(product.getNameZh());
        productVO.setReleaseDate(DateHelper.dateToString(product.getReleaseDate()));
        productVO.setCategory(EnumUtil.getAttribute(ProductCategory.class, product.getCategory()));
        productVO.setRemark(product.getRemark());

        //关联信息
        productVO.setFranchise(EntryUtil.getFranchise(product.getFranchise()));

        productVO.setOrganizations(JSON.parseArray(product.getOrganizations()));
        productVO.setStaffs(JSON.parseArray(product.getStaffs()));

        return productVO;
    }

    /**
     * Product转VO对象，用于list和index页面，转换量较少
     *
     * @param product 作品
     * @return ProductVOAlpha
     * @author rakbow
     */
    default ProductVOAlpha product2VOAlpha(Product product) {

        ProductVOAlpha productVOAlpha = new ProductVOAlpha();

        //基础信息
        productVOAlpha.setId(product.getId());
        productVOAlpha.setName(product.getName());
        productVOAlpha.setNameEn(product.getNameEn());
        productVOAlpha.setNameZh(product.getNameZh());
        productVOAlpha.setReleaseDate(DateHelper.dateToString(product.getReleaseDate()));
        productVOAlpha.setRemark(product.getRemark());

        //关联信息
        productVOAlpha.setCategory(EnumUtil.getAttribute(ProductCategory.class, product.getCategory()));

        //对封面图片进行处理
        JSONObject cover = new JSONObject();
        JSONArray images = JSON.parseArray(product.getImages());
        cover.put("url", QiniuImageUtil.getThumbUrlWidth(CommonConstant.EMPTY_IMAGE_WIDTH_URL, 50));
        cover.put("blackUrl", QiniuImageUtil.getThumbBackgroundUrl(CommonConstant.EMPTY_IMAGE_WIDTH_URL, 50));
        cover.put("name", "404");
        if (images.size() != 0) {
            for (int i = 0; i < images.size(); i++) {
                JSONObject image = images.getJSONObject(i);
                if (Objects.equals(image.getString("type"), "1")) {
                    cover.put("url", QiniuImageUtil.getThumbUrlWidth(image.getString("url"), 50));
                    cover.put("blackUrl", QiniuImageUtil.getThumbBackgroundUrl(image.getString("url"), 50));
                    cover.put("name", image.getString("nameEn"));
                }
            }
        }
        productVOAlpha.setCover(cover);

        productVOAlpha.setAddedTime(DateHelper.timestampToString(product.getAddedTime()));
        productVOAlpha.setEditedTime(DateHelper.timestampToString(product.getEditedTime()));
        productVOAlpha.setStatus(product.getStatus() == 1);

        return productVOAlpha;
    }

    /**
     * 列表，Product转VO对象，用于list和index页面，转换量较少
     *
     * @param products 作品列表
     * @return List<ProductVOAlpha>
     * @author rakbow
     */
    default List<ProductVOAlpha> product2VOAlpha(List<Product> products) {

        List<ProductVOAlpha> productVOAlphas = new ArrayList<>();

        if(!products.isEmpty()) {
            products.forEach(product -> productVOAlphas.add(product2VOAlpha(product)));
        }

        return productVOAlphas;
    }

    /**
     * 转VO对象，用于存储到搜索引擎
     *
     * @param product 商品
     * @return ProductVOBeta
     * @author rakbow
     */
    default ProductVOBeta product2VOBeta(Product product) {
        if (product == null) {
            return null;
        }

        ProductVOBeta productVOBeta = new ProductVOBeta();

        //基础信息
        productVOBeta.setId(product.getId());
        productVOBeta.setName(product.getName());
        productVOBeta.setNameEn(product.getNameEn());
        productVOBeta.setNameZh(product.getNameZh());
        productVOBeta.setReleaseDate(DateHelper.dateToString(product.getReleaseDate()));
        productVOBeta.setCategory(EnumUtil.getAttribute(ProductCategory.class, product.getCategory()));

        //关联信息
        productVOBeta.setFranchise(EntryUtil.getFranchise(product.getFranchise()));

        productVOBeta.setCover(QiniuImageUtil.getThumb70Url(product.getImages()));

        return productVOBeta;
    }

    /**
     * 列表转换, 转VO对象，用于存储到搜索引擎
     *
     * @param products 列表
     * @return List<ProductVOBeta>
     * @author rakbow
     */
    default List<ProductVOBeta> product2VOBeta(List<Product> products) {
        List<ProductVOBeta> productVOBetas = new ArrayList<>();

        if (!products.isEmpty()) {
            products.forEach(product -> {
                productVOBetas.add(product2VOBeta(product));
            });
        }

        return productVOBetas;
    }

}
