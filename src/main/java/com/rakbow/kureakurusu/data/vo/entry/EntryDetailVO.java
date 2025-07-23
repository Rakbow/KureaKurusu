package com.rakbow.kureakurusu.data.vo.entry;

import com.rakbow.kureakurusu.data.PageTraffic;
import lombok.*;

/**
 * @author Rakbow
 * @since 2025/1/7 0:37
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EntryDetailVO {

    private EntryVO entry;
    private PageTraffic traffic;
    private String cover;

}
