package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.dto.Query;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedHashMap;

@EqualsAndHashCode(callSuper = true)
@Data
public class ListQry extends Query {

    private int first;
    private int rows;
    private String sortField;
    private int sortOrder;
    private LinkedHashMap<String, LinkedHashMap<String, Object>> filters;

}
