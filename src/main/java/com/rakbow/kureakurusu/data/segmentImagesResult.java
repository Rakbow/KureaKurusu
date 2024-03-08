package com.rakbow.kureakurusu.data;

import com.rakbow.kureakurusu.data.vo.ImageVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2022-12-31 1:18
 */
@Data
public class segmentImagesResult {

    //添加了缩略图的images
    private List<ImageVO> images;
    //封面图片
    private ImageVO cover;
    //展示图片
    private List<ImageVO> displayImages;
    //其他图片
    private List<ImageVO> otherImages;

    public segmentImagesResult() {
        this.images = new ArrayList<>();
        this.cover = new ImageVO();
        this.displayImages = new ArrayList<>();
        this.otherImages = new ArrayList<>();
    }

    public void setCoverUrl(String url) {
        this.cover.setUrl(url);
    }

    public void addDisplayImage(ImageVO image) {
        this.displayImages.add(image);
    }

    public void addImage(ImageVO image) {
        this.images.add(image);
    }

    public void addOtherImage(ImageVO image) {
        this.otherImages.add(image);
    }

}
