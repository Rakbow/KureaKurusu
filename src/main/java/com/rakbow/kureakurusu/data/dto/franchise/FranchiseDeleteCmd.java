package com.rakbow.kureakurusu.data.dto.franchise;

import com.rakbow.kureakurusu.data.dto.base.CommonCommand;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/01/28 18:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FranchiseDeleteCmd extends CommonCommand {

    List<Long> ids;

}
