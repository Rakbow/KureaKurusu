package com.rakbow.kureakurusu.data.dto.product;

import com.rakbow.kureakurusu.data.dto.base.CommonCommand;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/01/17 21:53
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProductDeleteCmd extends CommonCommand {

    List<Long> ids;

}
