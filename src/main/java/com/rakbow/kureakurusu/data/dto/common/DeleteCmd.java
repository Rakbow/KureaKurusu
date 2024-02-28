package com.rakbow.kureakurusu.data.dto.common;

import com.rakbow.kureakurusu.data.dto.base.CommonCommand;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/2/28 15:51
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeleteCmd extends CommonCommand {

    List<Long> ids;

}
