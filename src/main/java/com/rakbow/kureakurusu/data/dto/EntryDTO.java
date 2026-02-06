package com.rakbow.kureakurusu.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rakbow.kureakurusu.data.entity.Entry;
import com.rakbow.kureakurusu.toolkit.convert.GlobalConverters;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Rakbow
 * @since 2026/2/7 0:45
 */
public class EntryDTO {

    @AutoMapper(target = Entry.class, reverseConvertGenerate = false, uses = GlobalConverters.class)
    public record EntryCreateDTO(
            long id,
            Integer type,
            Integer subType,
            @NotBlank(message = "{entity.crud.name.required_field}")
            String name,
            String nameZh,
            String nameEn,
            List<String> aliases,
            List<String> links,
            int gender,
            String date,
            String detail,
            String remark
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @AutoMapper(target = Entry.class, reverseConvertGenerate = false)
    public record EntryUpdateDTO(
            Long id,
            @NotBlank(message = "{entity.crud.name.required_field}")
            String name,
            String nameZh,
            String nameEn,
            String date,
            Integer gender,
            List<String> aliases,
            List<String> links,
            String remark
    ) {
    }

    public record EntrySuperCreateDTO(
            EntryDTO.EntryCreateDTO entry,
            List<ImageDTO.ImageMiniDTO> images,
            List<RelatedEntityMiniDTO> relatedEntries
    ) {
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class EntryListQueryDTO extends ListQueryDTO {

        private int type;

        public void init() {
            type = super.getVal("type");
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EntrySearchQueryDTO extends ListQueryDTO {

        private Integer type;
        private Integer subType;
        private String keyword;
        private List<String> keywords = new ArrayList<>();

        public void init() {
            type = super.getVal("type");
            subType = super.getVal("subType");
            keyword = super.getVal("keyword");
            if (Objects.nonNull(keyword)) {
                if (keyword.contains(",")) {
                    keywords = Arrays.stream(super.getKeyword().split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .toList();
                } else {
                    keywords = new ArrayList<>();
                    keywords.add(keyword);
                }
            } else {
                keywords = new ArrayList<>();
            }
        }

    }

}
