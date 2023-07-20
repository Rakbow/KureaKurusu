package com.rakbow.kureakurusu.dao;

import com.rakbow.kureakurusu.entity.Franchise;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2022-08-20 1:06
 * @Description: 系列CRUD
 */
@Mapper
public interface FranchiseMapper {

    //新增系列
    int addFranchise(Franchise franchise);

    //通过id查找系列
    Franchise getFranchise(int id, boolean status);

    //根据过滤条件搜索Book
    List<Franchise> getFranchisesByFilter(String name, String nameZh, String isMeta, boolean status,
                                          String sortField, int sortOrder, int first, int row);

    //超详细查询条数
    int getFranchisesRowsByFilter(String name, String nameZh, String isMeta, boolean status);

    //修改系列信息
    void updateFranchise(int id, Franchise franchise);

    //删除系列
    int deleteFranchise(int id);

    //获取所有系列
    List<Franchise> getAll();

    void updateMetaInfo(int id, String metaInfo);

    //根据父系列id获取子系列
    List<Franchise> getFranchisesByParentId(String parentId);

}
