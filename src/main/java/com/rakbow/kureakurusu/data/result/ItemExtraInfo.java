package com.rakbow.kureakurusu.data.result;

import com.rakbow.kureakurusu.data.vo.relation.RelationVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2024/7/27 3:21
 */
@Data
public class ItemExtraInfo {

    private List<RelationVO> classifications = new ArrayList<>();
    private List<RelationVO> events = new ArrayList<>();
    private List<RelationVO> materials = new ArrayList<>();

}
