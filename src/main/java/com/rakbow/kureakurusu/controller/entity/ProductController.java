package com.rakbow.kureakurusu.controller.entity;

import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.system.ApiResult;
import com.rakbow.kureakurusu.data.dto.QueryParams;
import com.rakbow.kureakurusu.data.dto.base.ListQry;
import com.rakbow.kureakurusu.data.dto.product.ProductAddDTO;
import com.rakbow.kureakurusu.data.dto.product.ProductDeleteCmd;
import com.rakbow.kureakurusu.data.dto.product.ProductDetailQry;
import com.rakbow.kureakurusu.data.dto.product.ProductUpdateDTO;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.vo.product.ProductDetailVO;
import com.rakbow.kureakurusu.data.entity.Product;
import com.rakbow.kureakurusu.service.*;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.convertMapper.entity.ProductVOMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

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
    private final ProductService srv;
    private final PersonService personSrv;
    private final ProductVOMapper VOMapper;
    private final int ENTITY_VALUE = Entity.PRODUCT.getValue();
    //endregion

    //region ------curd------

    @PostMapping("detail")
    @UniqueVisitor
    public ApiResult getProductDetail(@RequestBody ProductDetailQry qry) {
        ApiResult res = new ApiResult();
        try {
            ProductDetailVO vo = srv.getDetail(qry);
            vo.setPersonnel(personSrv.getPersonnel(ENTITY_VALUE, qry.getId()));
            res.loadData(vo);
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage());
        }
        return res;
    }

    @PostMapping("list")
    public ApiResult getProducts(@RequestBody ListQry qry) {
        ApiResult res = new ApiResult();
        try {
            res.data = srv.getProducts(new QueryParams(qry));
        } catch (Exception e) {
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
            srv.save(product);
            res.ok(I18nHelper.getMessage("entity.curd.insert.success", Entity.PRODUCT.getName()));
        } catch (Exception e) {
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
            srv.updateProduct(dto);
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
            srv.deleteProducts(cmd.getIds());
            res.ok(I18nHelper.getMessage("entity.curd.delete.success", Entity.PRODUCT.getName()));
        } catch (Exception e) {
            res.fail(e);
            log.error(e.getMessage());
        }
        return res;
    }

    //endregion

}
