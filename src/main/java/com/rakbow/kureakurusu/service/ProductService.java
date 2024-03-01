package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.EpisodeMapper;
import com.rakbow.kureakurusu.dao.PersonRelationMapper;
import com.rakbow.kureakurusu.dao.ProductMapper;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.SimpleSearchParam;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.dto.product.ProductDetailQry;
import com.rakbow.kureakurusu.data.dto.product.ProductUpdateDTO;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.emun.entity.product.ProductCategory;
import com.rakbow.kureakurusu.data.entity.Episode;
import com.rakbow.kureakurusu.data.entity.PersonRelation;
import com.rakbow.kureakurusu.data.entity.Product;
import com.rakbow.kureakurusu.data.vo.episode.EpisodeVOAlpha;
import com.rakbow.kureakurusu.data.vo.product.ProductDetailVO;
import com.rakbow.kureakurusu.data.vo.product.ProductMiniVO;
import com.rakbow.kureakurusu.data.vo.product.ProductVOAlpha;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.common.EntityUtil;
import com.rakbow.kureakurusu.util.common.VisitUtil;
import com.rakbow.kureakurusu.util.convertMapper.entity.EpisodeVOMapper;
import com.rakbow.kureakurusu.util.convertMapper.entity.ProductVOMapper;
import com.rakbow.kureakurusu.util.file.CommonImageUtil;
import com.rakbow.kureakurusu.util.file.QiniuFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Rakbow
 * @since 2022-08-20 2:02
 */
@Service
@RequiredArgsConstructor
public class ProductService extends ServiceImpl<ProductMapper, Product> {

    //region ------inject------
    private final ProductMapper mapper;
    private final EpisodeMapper epMapper;
    private final PersonRelationMapper relationMapper;
    private final QiniuFileUtil qiniuFileUtil;
    private final VisitUtil visitUtil;
    private final EntityUtil entityUtil;
    private final ProductVOMapper VOMapper;
    private final EpisodeVOMapper epVOMapper;
    //endregion

    //region const
    private final int ENTITY_VALUE = Entity.PRODUCT.getValue();
    private static final ProductCategory[] VIDEO_PRODUCT = {
            ProductCategory.ANIME_TV,
            ProductCategory.ANIME_MOVIE,
            ProductCategory.LIVE_ACTION_MOVIE,
            ProductCategory.TV_SERIES, ProductCategory.OVA_OAD
    };

    //endregion

    //region ------basic crud------

    @Transactional
    public SearchResult<ProductMiniVO> searchProducts(SimpleSearchParam param) {

        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<Product>()
                .or().like(Product::getName, param.getKeyword())
                .or().like(Product::getNameZh, param.getKeyword())
                .or().like(Product::getNameEn, param.getKeyword())
                .eq(Product::getStatus, 1)
                .orderByDesc(Product::getId);

        IPage<Product> pages = mapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);

        List<ProductMiniVO> items = VOMapper.toMiniVO(pages.getRecords());

        return new SearchResult<>(items, pages.getTotal(), pages.getCurrent(), pages.getSize());
    }

    @Transactional
    public SearchResult<ProductVOAlpha> getProducts(QueryParams param) {
        String name = param.getStr("name");
        String nameEn = param.getStr("nameEn");
        String nameZh = param.getStr("nameZh");

        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<Product>()
                .like(!StringUtils.isBlank(name), Product::getName, name)
                .like(!StringUtils.isBlank(nameZh), Product::getNameZh, nameZh)
                .like(!StringUtils.isBlank(nameEn), Product::getNameEn, nameEn);

        List<Integer> categories = param.getArray("category");
        if(categories != null && !categories.isEmpty())
            wrapper.in(Product::getCategory, categories);

        if(!StringUtils.isBlank(param.sortField)) {
            switch (param.sortField) {
                case "name" -> wrapper.orderBy(true, param.asc(), Product::getName);
                case "nameZh" -> wrapper.orderBy(true, param.asc(), Product::getNameZh);
                case "nameEn" -> wrapper.orderBy(true, param.asc(), Product::getNameEn);
                case "releaseDate" -> wrapper.orderBy(true, param.asc(), Product::getReleaseDate);
                case "category" -> wrapper.orderBy(true, param.asc(), Product::getCategory);
                case "franchise" -> wrapper.orderBy(true, param.asc(), Product::getFranchise);
            }
        }else{
            wrapper.orderByDesc(Product::getId);
        }

        IPage<Product> pages = mapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);
        List<ProductVOAlpha> items = VOMapper.toVOAlpha(pages.getRecords());

        return new SearchResult<>(items, pages.getTotal(), pages.getCurrent(), pages.getSize());
    }

    @SneakyThrows
    @Transactional
    public ProductDetailVO getDetail(ProductDetailQry qry) {
        Product product = getById(qry.getId());
        if(product == null)
            throw new Exception(I18nHelper.getMessage("entity.url.error", Entity.PRODUCT.getName()));

        return ProductDetailVO.builder()
                .item(VOMapper.toVO(product))
                .options(entityUtil.getDetailOptions(ENTITY_VALUE))
                .traffic(entityUtil.getPageTraffic(ENTITY_VALUE, qry.getId()))
                .itemImageInfo(CommonImageUtil.segmentImages(product.getImages(), 100, Entity.PRODUCT, true))
                .episodes(getEpisodes(product.getId(), product.getCategory()))
                .build();
    }

    @Transactional
    public void updateProduct(ProductUpdateDTO dto) {
        mapper.update(new LambdaUpdateWrapper<Product>()
                .eq(Product::getId, dto.getId())
                .set(Product::getName, dto.getName())
                .set(Product::getNameZh, dto.getNameEn())
                .set(Product::getNameZh, dto.getNameEn())
                .set(Product::getReleaseDate, dto.getReleaseDate())
                .set(Product::getFranchise, dto.getFranchise())
                .set(Product::getCategory, dto.getCategory())
                .set(Product::getRemark, dto.getRemark())
                .set(Product::getEditedTime, DateHelper.now())
        );
    }

    @Transactional
    public void deleteProducts(List<Long> ids) {
        //get original data
        List<Product> products = mapper.selectBatchIds(ids);
        for (Product product : products) {
            //delete all image
            qiniuFileUtil.deleteAllImage(product.getImages());
            //delete product
            mapper.deleteById(product.getId());
            //delete visit record
            visitUtil.deleteVisit(ENTITY_VALUE, product.getId());
            //delete person relation
            relationMapper.delete(new LambdaQueryWrapper<PersonRelation>().eq(PersonRelation::getEntityId, product.getId()));
        }
    }

    //endregion

    //region other

    private List<EpisodeVOAlpha> getEpisodes(long productId, ProductCategory category) {
        List<EpisodeVOAlpha> res = new ArrayList<>();
        if(Arrays.asList(VIDEO_PRODUCT).contains(category)) {
            List<Episode> eps = epMapper.selectList(new LambdaQueryWrapper<Episode>().eq(Episode::getRelatedId, productId));
            res.addAll(epVOMapper.toVOAlpha(eps));
        }
        return res;
    }

    //endregion
}
