package com.rakbow.kureakurusu.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private List<String> keywords = new ArrayList<>();

    public void init() {
        super.init();
        type = super.getVal("type");
        if(ObjectUtils.isNotEmpty(super.getKeyword()))
            keywords = Arrays.stream(super.getKeyword().split(",")).toList();
    }

}
