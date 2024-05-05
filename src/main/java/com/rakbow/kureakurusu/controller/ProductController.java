package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.emun.Entity;
import com.rakbow.kureakurusu.data.entity.Product;
import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.vo.product.ProductDetailVO;
import com.rakbow.kureakurusu.service.PersonService;
import com.rakbow.kureakurusu.service.ProductService;
import com.rakbow.kureakurusu.util.I18nHelper;
import com.rakbow.kureakurusu.util.convertMapper.ProductVOMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * @author Rakbow
 * @since 2022-10-15 19:18
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("db/product")
public class ProductController {

    //region inject

    private final ProductService srv;
    private final PersonService personSrv;
    private final ProductVOMapper VOMapper;
    private final int ENTITY_VALUE = Entity.PRODUCT.getValue();
    //endregion

    //region curd

    @PostMapping("detail")
    @UniqueVisitor
    public ApiResult detail(@RequestBody ProductDetailQry qry) {
        ProductDetailVO vo = srv.getDetail(qry);
        vo.setPersonnel(personSrv.getPersonnel(ENTITY_VALUE, qry.getId()));
        return new ApiResult().load(vo);
    }

    @PostMapping("list")
    public ApiResult list(@RequestBody ListQueryDTO qry) {
        return new ApiResult().load(srv.getProducts(new ProductListParams(qry)));
    }

    @PostMapping("add")
    public ApiResult add(@Valid @RequestBody ProductAddDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //build
        Product product = VOMapper.build(dto);
        //save
        srv.save(product);
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.insert.success", Entity.PRODUCT.getName()));
    }

    @PostMapping("update")
    public ApiResult update(@Valid @RequestBody ProductUpdateDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //save
        srv.updateById(new Product(dto));
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.update.success", Entity.PRODUCT.getName()));
    }

    @DeleteMapping("delete")
    public ApiResult delete(@RequestBody DeleteCmd cmd) {
        srv.deleteProducts(cmd.getIds());
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.delete.success", Entity.PRODUCT.getName()));
    }

    @PostMapping("get-related-products")
    public ApiResult getRelatedProducts(@RequestBody CommonDetailQty qry) {
        return new ApiResult().load(srv.getRelatedProducts(qry.getId()));
    }

    //endregion

}
