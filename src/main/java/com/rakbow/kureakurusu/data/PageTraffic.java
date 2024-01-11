package com.rakbow.kureakurusu.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 页面流量实体类
 *
 * @author Rakbow
 * @since 2023-01-17 23:22
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageTraffic {

    private long visitCount;//浏览数
    private long likeCount;//点赞数
    private boolean liked;//是否点过赞

}
