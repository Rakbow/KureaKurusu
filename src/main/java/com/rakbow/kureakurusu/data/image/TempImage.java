package com.rakbow.kureakurusu.data.image;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rakbow.kureakurusu.data.emun.ImageType;
import com.rakbow.kureakurusu.data.vo.TempImageVO;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.Data;

/**
 * 通用图片信息
 *
 * @author Rakbow
 * @since 2023-02-08 18:04
 */
@Data
@AutoMappers({
        @AutoMapper(target = TempImageVO.class, reverseConvertGenerate = false)
})
public class TempImage {

    private String url;//图片url
    private String nameEn;//图片名(英)
    private String nameZh;//图片名(中)
    private int type;//图片类型
    private String description;//图片描述
    private String uploadTime;//图片上传时间
    private String uploadUser;

    public TempImage() {
        this.url = "";
        this.nameEn = "";
        this.nameZh = "";
        this.type = ImageType.DEFAULT.getValue();
        this.description = "";
        this.uploadTime = DateHelper.nowStr();
        this.uploadUser = "";
    }

    @JsonIgnore
    public boolean isMain() {
        return this.type == ImageType.MAIN.getValue();
    }

    @JsonIgnore
    public boolean isDisplay() {
        return this.type == ImageType.MAIN.getValue() || this.type == ImageType.DEFAULT.getValue();
    }

}