package com.rakbow.kureakurusu.data;

import com.rakbow.kureakurusu.data.image.Image;
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
    private List<Image> images;
    //展示图片
    private List<Image> displayImages;
    //其他图片
    private List<Image> otherImages;

    public segmentImagesResult() {
        this.images = new ArrayList<>();
        this.displayImages = new ArrayList<>();
        this.otherImages = new ArrayList<>();
    }

    public void addDisplayImage(Image image) {
        this.displayImages.add(image);
    }

    public void addImage(Image image) {
        this.images.add(image);
    }

    public void addOtherImage(Image image) {
        this.otherImages.add(image);
    }

}
