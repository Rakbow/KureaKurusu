package com.rakbow.kureakurusu.data.vo.entry;

import com.rakbow.kureakurusu.data.PageTraffic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Rakbow
 * @since 2024/7/2 21:46
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EntryDetailVO {

    private EntryVO item;
    private PageTraffic traffic;
    private Map<String, Object> options;

    private String cover;

}
