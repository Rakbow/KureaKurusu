package com.rakbow.kureakurusu.data.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Rakbow
 * @since 2024/5/24 16:03
 */
@Data
@NoArgsConstructor
public class ImageMiniDTO {

    private MultipartFile file;
    private String name;
    private int type;
    private String detail;
    private String base64Code;

    public ImageMiniDTO(ImageCreateDTO dto, MultipartFile file) {
        this.file = file;
        this.name = dto.getName();
        this.type = dto.getType();
        this.detail = dto.getDetail();
    }

    public ImageMiniDTO(int type, MultipartFile file) {
        this.type = type;
        this.file = file;
    }

}
