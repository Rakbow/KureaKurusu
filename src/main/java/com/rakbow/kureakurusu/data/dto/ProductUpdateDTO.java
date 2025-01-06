package com.rakbow.kureakurusu.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.entity.entry.Product;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/01/17 21:53
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AutoMappers({
        @AutoMapper(target = Product.class, reverseConvertGenerate = false)
})
public class ProductUpdateDTO extends EntryUpdateDTO {

    private Integer type;
    private String date;// event-date

    public ProductUpdateDTO() {
        setEntityType(EntityType.PRODUCT.getValue());
        type = 0;
        date = "";
    }

}
