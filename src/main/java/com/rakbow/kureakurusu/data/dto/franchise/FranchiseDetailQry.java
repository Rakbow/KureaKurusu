package com.rakbow.kureakurusu.data.dto.franchise;

import com.rakbow.kureakurusu.data.dto.base.DetailQry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Rakbow
 * @since 2024/01/26 0:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FranchiseDetailQry extends DetailQry {

    private Long id;

}
