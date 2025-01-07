package com.rakbow.kureakurusu.data.vo.item;

import com.rakbow.kureakurusu.data.PageTraffic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rakbow
 * @since 2024/4/25 10:07
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemDetailVO {

    private ItemVO item;
    private int type;
    private PageTraffic traffic;
    private String cover;
}
