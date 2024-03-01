package com.rakbow.kureakurusu.data.dto.common;

import com.rakbow.kureakurusu.data.dto.base.DetailQry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Rakbow
 * @since 2024/3/1 17:48
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonDetailQty extends DetailQry {

    private long id;

}
