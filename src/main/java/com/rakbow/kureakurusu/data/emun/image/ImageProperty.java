package com.rakbow.kureakurusu.data.emun.image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2022-11-08 23:35
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageProperty {

    @Getter
    private long size;
    @Getter
    private int height;
    @Getter
    private int width;

}
