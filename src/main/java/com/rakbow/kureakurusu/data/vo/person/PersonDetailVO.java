package com.rakbow.kureakurusu.data.vo.person;

import com.rakbow.kureakurusu.data.PageTraffic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author Rakbow
 * @since 2024-01-01 18:10
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonDetailVO {

    private PersonVO item;
    private String cover;
    private PageTraffic traffic;
    private Map<String, Object> options;

}
