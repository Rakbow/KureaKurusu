package com.rakbow.kureakurusu.data;

import lombok.Data;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-01-17 23:22
 * @Description: 页面信息实体类
 */
@Data
public class pageInfo {

    private String addedTime;//收录时间
    private String editedTime;//编辑时间
    private long visitCount;//浏览数
    private long likeCount;//点赞数
    private boolean liked;//是否点过赞

}
