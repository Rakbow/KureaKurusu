package com.rakbow.kureakurusu.data.dto;

import com.rakbow.kureakurusu.data.entity.resource.Image;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Rakbow
 * @since 2026/2/7 1:00
 */
public class ImageDTO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ImageListQueryDTO extends ListQueryDTO {

        private Integer type;
        private Integer relEntityType;
        private Integer relEntityId;

        public void init() {
            this.type = super.getVal("type");
            this.relEntityType = super.getVal("entityType");
            this.relEntityId = super.getVal("entityId");
        }

    }

    @Data
    @NoArgsConstructor
    public static class ImageMiniDTO {

        private MultipartFile file;
        private String name;
        private int type;
        private String detail;
        private String base64Code;

        public ImageMiniDTO(ImageCreateDTO dto, MultipartFile file) {
            this.file = file;
            this.name = dto.name();
            this.type = dto.type();
            this.detail = dto.detail();
        }

        public ImageMiniDTO(int type, MultipartFile file) {
            this.type = type;
            this.file = file;
        }

    }

    public record ImagePreviewDTO(int entityType, long entityId, int count) {
    }

    @AutoMapper(target = Image.class, reverseConvertGenerate = false)
    public record ImageUpdateDTO(
            long id,
            int type,
            @NotBlank(message = "{entity.crud.name.required_field}")
            String name,
            String nameZh,
            String detail
    ) {
    }

    public record ImageCreateDTO(String name, int type, String detail) {
    }

    public record ImageDeleteDTO(
            int entityType,
            long entityId,
            List<ImageDeleteMiniDTO> images
    ) {
    }

    public record ImageDeleteMiniDTO(
            long id,
            String url
    ) {
    }

}
