package com.rakbow.kureakurusu.data.vo.book;

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
 * @since 2024/3/4 11:33
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookDetailVO {

    private BookVO item;

    private PageTraffic traffic;
    private segmentImagesResult itemImageInfo;
    private PersonnelStruct personnel;
    private Map<String, Object> options;

}
