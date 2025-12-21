package com.rakbow.kureakurusu.data.vo.item;

import com.rakbow.kureakurusu.data.enums.ItemSubType;
import com.rakbow.kureakurusu.data.enums.ItemType;
import com.rakbow.kureakurusu.toolkit.convert.GlobalConverters;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.github.linpeilie.annotations.AutoMapping;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2025/7/14 22:51
 */
@Data
@AutoMappers({
        @AutoMapper(target = ItemSearchVO.class, reverseConvertGenerate = false, uses = GlobalConverters.class)
})
public class ItemSimpleVO {

    private Long id;
    private ItemType type;
    private ItemSubType subType;
    private String name;
    private String releaseDate;
    private String barcode;
    private String catalogId;
    private double price;
    @AutoMapping(target = "currency", qualifiedByName = "getCurrency")
    private String region;

}
