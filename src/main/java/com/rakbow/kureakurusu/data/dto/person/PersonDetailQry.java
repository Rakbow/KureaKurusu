package com.rakbow.kureakurusu.data.dto.person;

import com.rakbow.kureakurusu.data.dto.DetailQry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-12-23 11:40
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDetailQry extends DetailQry {

    private Long id;

}
