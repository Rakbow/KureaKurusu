package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.entity.Product;
import com.rakbow.kureakurusu.data.vo.product.ProductDetailVO;
import com.rakbow.kureakurusu.service.ProductService;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import io.github.linpeilie.Converter;
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

    private final ProductService srv;
    private final Converter converter;

    @PostMapping("detail")
    @UniqueVisitor
    public ApiResult detail(@RequestBody CommonDetailQry qry) {
        ProductDetailVO vo = srv.getDetail(qry.getId());
        return new ApiResult().load(vo);
    }

    @PostMapping("list")
    public ApiResult list(@RequestBody ListQueryDTO qry) {
        return new ApiResult().load(srv.list(new ProductListParams(qry)));
    }

    @PostMapping("add")
    public ApiResult add(@Valid @RequestBody ProductCreateDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //build
        Product product = converter.convert(dto, Product.class);
        //save
        srv.save(product);
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.insert.success", EntityType.PRODUCT.getLabel()));
    }

    @PostMapping("update")
    public ApiResult update(@Valid @RequestBody ProductUpdateDTO dto, BindingResult errors) {
        //check
        if (errors.hasErrors()) return new ApiResult().fail(errors);
        //build
        Product product = converter.convert(dto, Product.class);
        //save
        srv.updateById(product);
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.update.success", EntityType.PRODUCT.getLabel()));
    }

    @DeleteMapping("delete")
    public ApiResult delete(@RequestBody DeleteCmd cmd) {
        srv.deleteProducts(cmd.getIds());
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.delete.success", EntityType.PRODUCT.getLabel()));
    }

    @PostMapping("get-sub-product")
    public ApiResult getSubProduct(@RequestBody CommonDetailQry qry) {
        return new ApiResult().load(srv.getSubProducts(qry.getId()));
    }

}
