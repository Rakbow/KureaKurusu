package com.rakbow.kureakurusu.data.dto;

/**
 * @author Rakbow
 * @since 2026/4/16 22:03
 */
public class IndexDTO {

    public record IndexUpdateDTO(Long id, String name, String remark) {
    }

    public record IndexElementUpdateDTO(Long indexElementId, String remark) {
    }

}
