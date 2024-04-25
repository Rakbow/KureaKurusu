package com.rakbow.kureakurusu.data.vo;

import com.rakbow.kureakurusu.data.PageTraffic;
import com.rakbow.kureakurusu.data.person.PersonnelStruct;
import com.rakbow.kureakurusu.data.segmentImagesResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Rakbow
 * @since 2024/4/25 10:07
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemDetailVO {

    private Object item;
    private PageTraffic traffic;
    private segmentImagesResult itemImageInfo;
    private PersonnelStruct personnel;
    private Map<String, Object> options;

}
