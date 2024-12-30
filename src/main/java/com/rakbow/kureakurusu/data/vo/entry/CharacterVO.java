package com.rakbow.kureakurusu.data.vo.entry;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.vo.LinkVO;
import lombok.Data;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/12/28 4:47
 */
@Data
public class CharacterVO {

    private long id;
    private String name;
    private String nameZh;
    private String nameEn;
    private String birthDate;
    private Attribute<Integer> gender;
    private List<LinkVO> links;
    private List<String> aliases;
    private String detail;
    private String remark;
    private Boolean status;
    private String addedTime;
    private String editedTime;

}
