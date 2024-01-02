package com.rakbow.kureakurusu.data.vo.person;

import com.rakbow.kureakurusu.data.PageTraffic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2024-01-01 18:10
 * @Description:
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PersonDetailVO {

    private PersonVO item;
    private PageTraffic traffic;

}
