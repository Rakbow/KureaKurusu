package com.rakbow.kureakurusu.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/6/30 0:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationListQueryDTO extends ListQueryDTO {

    private int entityType;
    private long entityId;

    private int targetEntityType;
    private List<Integer> targetEntitySubTypes;

}