package com.rakbow.kureakurusu.data.dto.common;

import com.rakbow.kureakurusu.data.dto.base.CommonCommand;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Rakbow
 * @since 2024/01/07 0:13
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateDetailCmd extends CommonCommand {

    private int entityType;
    private long entityId;
    private String text;

}
