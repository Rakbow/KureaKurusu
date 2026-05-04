package com.rakbow.kureakurusu.data.dto;

/**
 * @author Rakbow
 * @since 2026/5/4 16:56
 */
public class ResourceDTO {

    public record ResourceInfoCommonDTO(
            long id,
            int entityType,
            long entityId,
            String path,
            String remark,
            int flag
    ) {
    }

}
