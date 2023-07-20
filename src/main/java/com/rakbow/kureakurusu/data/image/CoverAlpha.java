package com.rakbow.kureakurusu.data.image;

import lombok.Data;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-05-17 1:19
 * @Description:
 */
@Data
public class CoverAlpha {

    private String url;
    private String thumbUrl;
    private String thumbUrl70;
    private String blackUrl;
    private String name;

    public CoverAlpha() {
        url = "";
        thumbUrl = "";
        thumbUrl70 = "";
        blackUrl = "";
        name = "";
    }

}
