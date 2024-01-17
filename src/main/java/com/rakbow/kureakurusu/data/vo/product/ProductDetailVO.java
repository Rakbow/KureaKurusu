package com.rakbow.kureakurusu.data.vo.product;

import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.PageTraffic;
import com.rakbow.kureakurusu.data.person.PersonnelStruct;
import com.rakbow.kureakurusu.data.segmentImagesResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rakbow
 * @since 2024/01/18 1:14
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDetailVO {

    private ProductVO product;
    private PageTraffic pageInfo;
    private segmentImagesResult itemImageInfo;
    private PersonnelStruct personnel;

    private JSONObject options;

}
