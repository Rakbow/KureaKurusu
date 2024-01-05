package com.rakbow.kureakurusu.dao;

import com.rakbow.kureakurusu.data.entity.Merch;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Rakbow
 * @since 2023-01-04 14:12
 */

@Mapper
public interface MerchMapper {

    //通过id查询Merch
    Merch getMerch(int id, boolean status);

    List<Merch> getMerchs(List<Integer> ids);

    List<Merch> getAll();

    //根据过滤条件搜索Merch
    List<Merch> getMerchsByFilter(String name, String barcode, List<Integer> franchises, List<Integer> products,
                                  int category, String region, String notForSale, boolean status,
                                  String sortField, int sortOrder, int first, int row);

    //超详细查询条数
    int getMerchsRowsByFilter(String name, String barcode, List<Integer> franchises, List<Integer> products,
                              int category, String region, String notForSale, boolean status);

    //新增Merch
    int addMerch (Merch merch);

    //更新Merch基础信息
    void updateMerch (int id, Merch merch);

    //删除单个Merch
    void deleteMerch(int id);

    //获取最新添加Merch, limit
    List<Merch> getMerchsOrderByAddedTime(int limit);

    //简单搜索
    List<Merch> simpleSearch(String keyWorld, int limit, int offset);

    int simpleSearchCount(String keyWorld);

}
