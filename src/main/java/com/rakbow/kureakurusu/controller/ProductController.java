package com.rakbow.kureakurusu.controller;

import com.rakbow.kureakurusu.annotation.UniqueVisitor;
import com.rakbow.kureakurusu.data.common.ApiResult;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.entity.Product;
import com.rakbow.kureakurusu.data.vo.product.ProductDetailVO;
import com.rakbow.kureakurusu.service.PersonService;
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

    //region inject

    private final ProductService srv;
    private final PersonService personSrv;
    private final Converter converter;
    private final int ENTITY_VALUE = EntityType.PRODUCT.getValue();
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
        //save
        srv.updateById(new Product(dto));
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.update.success", EntityType.PRODUCT.getLabel()));
    }

    @DeleteMapping("delete")
    public ApiResult delete(@RequestBody DeleteCmd cmd) {
        srv.deleteProducts(cmd.getIds());
        return new ApiResult().ok(I18nHelper.getMessage("entity.curd.delete.success", EntityType.PRODUCT.getLabel()));
    }

    @PostMapping("get-related-products")
    public ApiResult getRelatedProducts(@RequestBody CommonDetailQry qry) {
        return new ApiResult().load(srv.getRelatedProducts(qry.getId()));
    }

    //endregion

}
