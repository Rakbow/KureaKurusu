package com.rakbow.kureakurusu.controller.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.ApiResult;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.dto.album.AlbumDeleteCmd;
import com.rakbow.kureakurusu.data.dto.album.AlbumUpdateDTO;
import com.rakbow.kureakurusu.data.dto.product.ProductAddDTO;
import com.rakbow.kureakurusu.data.dto.product.ProductDeleteCmd;
import com.rakbow.kureakurusu.data.dto.product.ProductDetailQry;
import com.rakbow.kureakurusu.data.dto.product.ProductUpdateDTO;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.entity.Album;
import com.rakbow.kureakurusu.data.vo.product.ProductDetailVO;
import com.rakbow.kureakurusu.data.vo.product.ProductVOAlpha;
import com.rakbow.kureakurusu.data.entity.Product;
import com.rakbow.kureakurusu.service.*;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.convertMapper.entity.ProductVOMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Rakbow
 * @since 2022-10-15 19:18
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/db/product")
public class ProductController {

    //region ------inject------

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final ProductService service;
    private final PersonService personService;
    private final ProductVOMapper VOMapper = ProductVOMapper.INSTANCES;
    private final int ENTITY_VALUE = Entity.PRODUCT.getValue();
    //endregion

    //region ------curd------

    @PostMapping("detail")
    @UniqueVisitor
    public ApiResult getProductDetail(@RequestBody ProductDetailQry qry) {
        ApiResult res = new ApiResult();
        try {
            ProductDetailVO vo = service.getDetail(qry);
            vo.setPersonnel(personService.getPersonnel(ENTITY_VALUE, qry.getId()));
            res.loadDate(vo);
        }catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage());
        }
        return res;
    }

    @PostMapping("add")
    public ApiResult addProduct(@Valid @RequestBody ProductAddDTO dto, BindingResult errors) {
        ApiResult res = new ApiResult();
        try {
            //check
            if (errors.hasErrors())
                return res.fail(errors);
            //build
            Product product = VOMapper.build(dto);
            //save
            service.save(product);
            res.ok(I18nHelper.getMessage("entity.curd.insert.success", Entity.PRODUCT.getName()));
        }catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage());
        }
        return res;
    }

    @PostMapping("update")
    public ApiResult updateProduct(@Valid @RequestBody ProductUpdateDTO dto, BindingResult errors) {
        ApiResult res = new ApiResult();
        try {
            //check
            if (errors.hasErrors())
                return res.fail(errors);
            //save
            service.updateProduct(dto);
            res.ok(I18nHelper.getMessage("entity.curd.update.success", Entity.PRODUCT.getName()));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage());
        }
        return res;
    }

    @DeleteMapping("delete")
    public ApiResult deleteProduct(@RequestBody ProductDeleteCmd cmd) {
        ApiResult res = new ApiResult();
        try {
            service.deleteProducts(cmd.getIds());
            res.ok(I18nHelper.getMessage("entity.curd.delete.success", Entity.PRODUCT.getName()));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage());
        }
        return res;
    }

    //endregion

}
