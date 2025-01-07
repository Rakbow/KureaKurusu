package com.rakbow.kureakurusu.data.vo;

import com.rakbow.kureakurusu.data.image.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/1/7 21:58
 */
@Data
@AllArgsConstructor
public class ImageDisplayVO {

    private List<Image> images;
    private int count;

}
