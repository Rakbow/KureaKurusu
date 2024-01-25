package com.rakbow.kureakurusu.data.vo.franchise;

import com.rakbow.kureakurusu.data.PageTraffic;
import com.rakbow.kureakurusu.data.segmentImagesResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/01/26 0:55
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FranchiseDetailVO {

    private FranchiseVO item;

    private PageTraffic traffic;
    private segmentImagesResult itemImageInfo;
    private List<Object> options;

}
