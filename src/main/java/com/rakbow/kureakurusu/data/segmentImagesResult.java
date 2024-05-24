package com.rakbow.kureakurusu.data;

import com.rakbow.kureakurusu.data.vo.TempImageVO;
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
    private List<TempImageVO> images;
    //封面图片
    private TempImageVO cover;
    //展示图片
    private List<TempImageVO> displayImages;
    //其他图片
    private List<TempImageVO> otherImages;

    public segmentImagesResult() {
        this.images = new ArrayList<>();
        this.cover = new TempImageVO();
        this.displayImages = new ArrayList<>();
        this.otherImages = new ArrayList<>();
    }

    public void setCoverUrl(String url) {
        this.cover.setUrl(url);
    }

    public void addDisplayImage(TempImageVO image) {
        this.displayImages.add(image);
    }

    public void addImage(TempImageVO image) {
        this.images.add(image);
    }

    public void addOtherImage(TempImageVO image) {
        this.otherImages.add(image);
    }

}
