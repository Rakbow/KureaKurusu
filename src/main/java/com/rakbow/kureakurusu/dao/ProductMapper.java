package com.rakbow.kureakurusu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rakbow.kureakurusu.data.entity.Product;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Rakbow
 * @since 2022-08-20 1:49
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    //通过id查找作品
    Product getProduct(int id, boolean status);

    List<Product> getProducts(List<Integer> ids);

    //获取所有作品
    List<Product> getAll();

    //新增产品
    int addProduct(Product product);

    //修改系列信息
    void updateProduct(int id, Product product);

    //删除产品
    int deleteProduct(int id);

    //更新staff
    void updateProductStaffs(int id, String staffs, Timestamp editedTime);

    //更新组织
    void updateProductOrganizations(int id, String organizations, Timestamp editedTime);

    //条件搜索
    List<Product> getProductsByFilter(String name, String nameZh, List<Integer> franchises, List<Integer> categories,

                                          boolean status, String sortField, int sortOrder, int first, int row);
    //条件搜索数量
    int getProductsRowsByFilter(String name, String nameZh, List<Integer> franchises, List<Integer> categories, boolean status);

}
