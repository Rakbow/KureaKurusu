package com.rakbow.kureakurusu.data.image;

import com.rakbow.kureakurusu.util.common.DateUtil;
import lombok.Data;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-02-08 18:04
 * @Description: 通用图片信息
 */
@Data
public class Image {

    private String url;//图片url
    private String nameEn;//图片名(英)
    private String nameZh;//图片名(中)
    private int type;//图片类型
    private String description;//图片描述
    private String uploadTime;//图片上传时间

    public Image() {
        this.url = "";
        this.nameEn = "";
        this.nameZh = "";
        this.type = 0;
        this.description = "";
        this.uploadTime = DateUtil.getCurrentTime();
    }

}
