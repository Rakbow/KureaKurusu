package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rakbow.kureakurusu.dao.ImageMapper;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.common.ActionResult;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.emun.ImageType;
import com.rakbow.kureakurusu.data.emun.ItemType;
import com.rakbow.kureakurusu.data.entity.Item;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.data.segmentImagesResult;
import com.rakbow.kureakurusu.data.vo.item.ItemMiniVO;
import com.rakbow.kureakurusu.toolkit.file.CommonImageUtil;
import com.rakbow.kureakurusu.toolkit.file.QiniuImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Rakbow
 * @since 2024/5/26 20:10
 */
@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ImageMapper imageMapper;
    private final QiniuImageUtil qiniuImageUtil;
    private final SqlSessionFactory sqlSessionFactory;

    /**
     * 根据实体类型和实体Id获取图片
     *
     * @param entityType,entityId 实体类型 实体id
     * @author rakbow
     */
    @Transactional
    public SearchResult<Image> getEntityImages(int entityType, long entityId, int page, int size) {
        LambdaQueryWrapper<Image> wrapper = new LambdaQueryWrapper<Image>()
                .eq(Image::getEntityType, entityType)
                .eq(Image::getEntityId, entityId);
        IPage<Image> pages = imageMapper.selectPage(new Page<>(page, size), wrapper);
        CommonImageUtil.generateThumb(pages.getRecords());
        return new SearchResult<>(pages.getRecords(), pages.getTotal());
    }

    /**
     * 新增图片
     *
     * @param entityType 实体类型
     * @param entityId   实体id
     * @param files     新增图片文件数组
     * @param images 新增图片json数据
     * @author rakbow
     */
    @SneakyThrows
    @Transactional
    public void addEntityImage(int entityType, long entityId, MultipartFile[] files, List<Image> images) {
        //upload to qiniu server
        ActionResult ar = qiniuImageUtil.commonAddImages(entityType, entityId, files, images);
        if(ar.state) {
            //set image info
            for (Image image : images) {
                image.setEntityType(EntityType.get(entityType));
                image.setEntityId(entityId);
            }
            //batch insert
            MybatisBatch.Method<Image> method = new MybatisBatch.Method<>(ImageMapper.class);
            MybatisBatch<Image> batchInsert = new MybatisBatch<>(sqlSessionFactory, images);
            batchInsert.execute(method.insert());
        }else {
            throw new Exception(ar.message);
        }
    }

    /**
     * 更新图片
     *
     * @author rakbow
     */
    @Transactional
    public void updateEntityImage(List<Image> images) {
        for (Image image : images) imageMapper.updateById(image);
    }

    /**
     * delete image
     *
     * @author rakbow
     */
    @Transactional
    public void deleteEntityImage(List<Image> images) {
        //delete from qiniu server
        List<Image> deleteImages = qiniuImageUtil.deleteImage(images);
        //delete from database
        imageMapper.deleteBatchIds(deleteImages.stream().map(Image::getId).toList());
    }

    public String getItemCover(ItemType type, long itemId) {
        Image cover = imageMapper.selectOne(
                new LambdaQueryWrapper<Image>()
                        .eq(Image::getEntityType, EntityType.ITEM)
                        .eq(Image::getEntityId, itemId)
                        .eq(Image::getType, ImageType.MAIN)
        );
        return CommonImageUtil.getItemCover(type, cover);
    }

    public String getEntityCover(EntityType type, long entityId) {
        Image cover = imageMapper.selectOne(
                new LambdaQueryWrapper<Image>()
                        .eq(Image::getEntityType, type)
                        .eq(Image::getEntityId, entityId)
                        .eq(Image::getType, ImageType.MAIN)
        );
        return CommonImageUtil.getEntityCover(type, cover);
    }

    public String getThumbCover(EntityType type, long entityId) {
        Image cover = imageMapper.selectOne(
                new LambdaQueryWrapper<Image>()
                        .eq(Image::getEntityType, type)
                        .eq(Image::getEntityId, entityId)
                        .eq(Image::getType, ImageType.MAIN)
        );
        return CommonImageUtil.getThumbCover(cover);
    }

}
