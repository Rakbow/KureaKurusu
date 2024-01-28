package com.rakbow.kureakurusu.data.vo.product;

import com.rakbow.kureakurusu.data.PageTraffic;
import com.rakbow.kureakurusu.data.person.PersonnelStruct;
import com.rakbow.kureakurusu.data.segmentImagesResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/01/18 1:14
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDetailVO {

    private ProductVO item;

    private PageTraffic traffic;
    private segmentImagesResult itemImageInfo;
    private PersonnelStruct personnel;
    private List<Object> options;



}
