package com.rakbow.kureakurusu.data.vo;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/12/13 15:35
 */
@Data
@AllArgsConstructor
public class LinksVO {

    private Attribute<Integer> type;
    private List<LinkVO> links;

}
