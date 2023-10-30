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

@AllArgsConstructor
enum LinkType {
    OFFICIAL(0,"官网","Official"),
    BLOG(1,"个人博客","Blog"),
    TWITTER(2,"推特","Twitter"),
    FACEBOOK(3,"脸书","Facebook"),
    FanClub(4,"粉丝应援","FanClub"),
    OTHER(99,"其他","Other");

    @Getter
    private final int id;
    @Getter
    private final String nameZh;
    @Getter
    private final String nameEn;
}
