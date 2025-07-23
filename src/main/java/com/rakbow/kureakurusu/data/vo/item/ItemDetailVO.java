package com.rakbow.kureakurusu.data.vo.item;

import com.rakbow.kureakurusu.data.PageTraffic;
import lombok.*;

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
    private PageTraffic traffic;
    private String cover;

}
