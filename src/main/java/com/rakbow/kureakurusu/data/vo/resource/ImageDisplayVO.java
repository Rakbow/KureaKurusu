package com.rakbow.kureakurusu.data.vo.resource;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/1/7 21:58
 */
@Data
@AllArgsConstructor
public class ImageDisplayVO {

    private List<ImageVO> images;
    private long count;

}
