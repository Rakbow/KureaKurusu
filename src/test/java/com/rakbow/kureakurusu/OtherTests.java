package com.rakbow.kureakurusu;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.rakbow.kureakurusu.dao.*;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.emun.common.RelatedType;
import com.rakbow.kureakurusu.data.entity.*;
import jakarta.annotation.Resource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Rakbow
 * @since 2024/3/3 23:26
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = KureaKurusuApp.class)
public class OtherTests {

    @Resource
    private AlbumMapper albumMapper;
    @Resource
    private BookMapper bookMapper;
    @Resource
    private EntityRelationMapper relationMapper;
    @Resource
    private PersonRelationMapper personRelationMapper;
    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Test
    public void batchAddRelation() {
        List<Album> albums = albumMapper.selectList(null);
        List<EntityRelation> addRelations = new ArrayList<>();

        int albumType = Entity.ALBUM.getValue();
        int proType = Entity.PRODUCT.getValue();


        for (Album album : albums) {
//            if(album.getProducts().isEmpty()) continue;

//            List<Long> productIds = album.getProducts();
            List<Long> productIds = new ArrayList<>();
            for (long proId : productIds) {
                long prodId = proId - 15;
                List<EntityRelation> tmpRelations = relationMapper.selectList(
                        new LambdaQueryWrapper<EntityRelation>()
                                .eq(EntityRelation::getEntityType, albumType)
                                .eq(EntityRelation::getEntityId, album.getId())
                                .eq(EntityRelation::getRelatedEntityType, proType)
                                .eq(EntityRelation::getRelatedEntityId, prodId)
                );
                if(!tmpRelations.isEmpty()) continue;

                EntityRelation relation = new EntityRelation();
                relation.setEntityType(albumType);
                relation.setEntityId(album.getId());
                relation.setRelatedEntityType(proType);
                relation.setRelatedEntityId(prodId);
                relation.setRelatedType(RelatedType.MAIN_ENTRY);
                addRelations.add(relation);
            }
        }
        MybatisBatch.Method<EntityRelation> method = new MybatisBatch.Method<>(EntityRelationMapper.class);
        MybatisBatch<EntityRelation> batchInsert = new MybatisBatch<>(sqlSessionFactory, addRelations);
        batchInsert.execute(method.insert());

    }

    @Test
    public void batchAddRelationBook() {
        List<Book> items = bookMapper.selectList(null);
        List<EntityRelation> addRelations = new ArrayList<>();

        int bookType = Entity.BOOK.getValue();
        int proType = Entity.PRODUCT.getValue();


        for (Book item : items) {
//            if(item.getProducts().isEmpty()) continue;

//            List<Long> productIds = item.getProducts();
            List<Long> productIds = new ArrayList<>();
//            List<Long> productIds = new ArrayList<>();
            for (long proId : productIds) {
                long prodId = proId - 14;
                List<EntityRelation> tmpRelations = relationMapper.selectList(
                        new LambdaQueryWrapper<EntityRelation>()
                                .eq(EntityRelation::getEntityType, bookType)
                                .eq(EntityRelation::getEntityId, item.getId())
                                .eq(EntityRelation::getRelatedEntityType, proType)
                                .eq(EntityRelation::getRelatedEntityId, prodId)
                );
                if(!tmpRelations.isEmpty()) continue;

                EntityRelation relation = new EntityRelation();
                relation.setEntityType(bookType);
                relation.setEntityId(item.getId());
                relation.setRelatedEntityType(proType);
                relation.setRelatedEntityId(prodId);
                relation.setRelatedType(RelatedType.MAIN_ENTRY);
                addRelations.add(relation);
            }
        }
        MybatisBatch.Method<EntityRelation> method = new MybatisBatch.Method<>(EntityRelationMapper.class);
        MybatisBatch<EntityRelation> batchInsert = new MybatisBatch<>(sqlSessionFactory, addRelations);
        batchInsert.execute(method.insert());

    }

    @Test
    public void batchAddPersonRelationBook() {

        List<Book> items = bookMapper.selectList(new LambdaQueryWrapper<Book>().ge(Book::getId, 96).le(Book::getId, 96));
        List<PersonRelation> addRelations = new ArrayList<>();

        int bookType = Entity.BOOK.getValue();

        long role1Id = 10;
        long person1Id = 2;
        long role2Id = 67;
        long person2Id = 11676;
        for (Book item : items) {
            PersonRelation r_1 = new PersonRelation();
            r_1.setPersonId(person1Id);
            r_1.setRoleId(role1Id);
            r_1.setEntityType(bookType);
            r_1.setEntityId(item.getId());
            addRelations.add(r_1);
            PersonRelation r_2 = new PersonRelation();
            r_2.setPersonId(person2Id);
            r_2.setRoleId(role2Id);
            r_2.setEntityType(bookType);
            r_2.setEntityId(item.getId());
            r_2.setMain(1);
            addRelations.add(r_2);
        }
        MybatisBatch.Method<PersonRelation> method = new MybatisBatch.Method<>(PersonRelationMapper.class);
        MybatisBatch<PersonRelation> batchInsert = new MybatisBatch<>(sqlSessionFactory, addRelations);
        batchInsert.execute(method.insert());
    }

    @Test
    public void mybatisPlusReflectTest() {
        String key = "birthDate";
        TableInfo tableInfo = TableInfoHelper.getTableInfo(Person.class);
        if (tableInfo != null) {
            TableFieldInfo fieldInfo = tableInfo.getFieldList().stream()
                    .filter(f -> f.getProperty().equals(key)) // 根据实体类属性名查找对应的字段信息
                    .findFirst()
                    .orElse(null);
            if (fieldInfo != null) {
                String columnName = fieldInfo.getColumn();
                System.out.println("实体类属性名: " + key + " 对应的表字段名为: " + columnName);
            }
        }
    }

}
