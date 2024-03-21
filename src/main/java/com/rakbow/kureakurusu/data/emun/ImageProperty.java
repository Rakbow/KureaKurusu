package com.rakbow.kureakurusu.data.emun;

import lombok.*;

/**
 * @author Rakbow
 * @since 2022-11-08 23:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageProperty {

    private long size;
    private int height;
    private int width;

}
