package com.rakbow.kureakurusu.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author Rakbow
 * @since 2023-10-06 4:14
 */
@Data
public class Link {

    private int type;
    private String url;

    public Link() {
        type = 0;
        url = "";
    }

}