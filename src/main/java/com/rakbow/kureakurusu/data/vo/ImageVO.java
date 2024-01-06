package com.rakbow.kureakurusu.data.vo;

import com.rakbow.kureakurusu.data.image.Image;
import lombok.*;

/**
 * 图片
 *
 * @author Rakbow
 * @since 2023-02-09 10:42
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ImageVO extends Image {

    private String name;
    private String thumbUrl70;//缩略图url(70px)
    private String thumbUrl50;//缩略图url(50px)
    private String blackUrl;//缩略图url(黑背景)

}