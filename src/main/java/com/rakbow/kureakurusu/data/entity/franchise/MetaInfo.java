package com.rakbow.kureakurusu.data.entity.franchise;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.entity.Franchise;
import lombok.Data;

import java.util.List;

/**
 * 系列的元系列相关信息
 *
 * @author Rakbow
 * @since 2023-02-08 18:04
 */
public class MetaInfo {
    public String isMeta;//是否为元系列
    public List<Integer> childFranchises;//子系列
    public String metaId;//所属元系列id

    public MetaInfo(String metaInfoJson) {
        JSONObject metaInfo = JSON.parseObject(metaInfoJson);
        this.isMeta = metaInfo.getString("isMeta");
        this.childFranchises = metaInfo.getList("childFranchises", Integer.class);
        this.metaId = metaInfo.getString("metaId");
    }

    public MetaInfo(String isMeta, List<Integer> childFranchises, String metaId) {
        this.isMeta = isMeta;
        this.childFranchises = childFranchises;
        this.metaId = metaId;
    }

}
