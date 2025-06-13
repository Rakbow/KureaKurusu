package com.rakbow.kureakurusu.data.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2025/6/13 12:55
 */
@Data
public class EntityResourceCount {

    private int entityType;
    @JsonProperty("entity_id")
    private long entityId;
    private int count;

}
