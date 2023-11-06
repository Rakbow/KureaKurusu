package com.rakbow.kureakurusu.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-10-06 4:14
 * @Description:
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