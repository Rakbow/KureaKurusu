package com.rakbow.kureakurusu.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Rakbow
 * @since 2025/1/20 3:46
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntrySearchQueryDTO extends ListQueryDTO {

    private Integer type;
    private String keyword;
    private List<String> keywords = new ArrayList<>();

    public void init() {
        type = super.getVal("type");
        keyword = super.getVal("keyword");
        if(Objects.nonNull(keyword))
            if(keyword.contains(",")) {
                keywords = Arrays.stream(super.getKeyword().split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .toList();
            }else {
                keywords.add(keyword);
            }
    }

}
