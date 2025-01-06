package com.rakbow.kureakurusu.data.vo.entry;

import com.rakbow.kureakurusu.data.PageTraffic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Rakbow
 * @since 2024/12/28 4:46
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CharacterDetailVO {

    private CharaVO target;
    private PageTraffic traffic;
    private String cover;

}
