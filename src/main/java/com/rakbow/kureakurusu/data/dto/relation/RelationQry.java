package com.rakbow.kureakurusu.data.dto.relation;

import com.rakbow.kureakurusu.data.dto.base.Query;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Rakbow
 * @since 2024/2/28 16:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationQry extends Query {

    private int entityType;
    private long entityId;

}
