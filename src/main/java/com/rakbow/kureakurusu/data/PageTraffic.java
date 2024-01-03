package com.rakbow.kureakurusu.data;

import lombok.Data;

/**
 * 页面流量实体类
 *
 * @author Rakbow
 * @since 2023-01-17 23:22
 */
@Data
public class PageTraffic {

    private long visitCount;//浏览数
    private long likeCount;//点赞数
    private boolean liked;//是否点过赞

}
