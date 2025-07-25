package com.rakbow.kureakurusu.data.vo.favList;

import com.rakbow.kureakurusu.data.Attribute;
import lombok.Data;

/**
 * @author Rakbow
 * @since 2025/7/25 14:06
 */
@Data
public class FavListVO {

    private Long id;
    private Attribute<Integer> type;
    private String name;
    private String creator;
    private String createTime;
    private String updateTime;
    private String remark;

}
