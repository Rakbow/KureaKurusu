package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rakbow.kureakurusu.dao.FileRelatedMapper;
import com.rakbow.kureakurusu.dao.FileInfoMapper;
import com.rakbow.kureakurusu.dao.ImageMapper;
import com.rakbow.kureakurusu.data.CommonConstant;
import com.rakbow.kureakurusu.data.RedisKey;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.ImageListQueryDTO;
import com.rakbow.kureakurusu.data.dto.ImageMiniDTO;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.emun.ImageType;
import com.rakbow.kureakurusu.data.entity.resource.FileRelated;
import com.rakbow.kureakurusu.data.entity.resource.FileInfo;
import com.rakbow.kureakurusu.data.entity.resource.Image;
import com.rakbow.kureakurusu.data.vo.ImageDisplayVO;
import com.rakbow.kureakurusu.toolkit.*;
import com.rakbow.kureakurusu.toolkit.file.CommonImageUtil;
import com.rakbow.kureakurusu.toolkit.file.QiniuImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * @author Rakbow
 * @since 2024/5/26 20:10
 */
@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ImageMapper imageMapper;
    private final FileInfoMapper fileInfoMapper;
    private final FileRelatedMapper fileRelatedMapper;
    private final QiniuImageUtil qiniuImageUtil;
    private final SqlSessionFactory sqlSessionFactory;
    private final RedisUtil redisUtil;
    private final Tika tika = new Tika();

    @Value("${kureakurusu.path.file}")
    private String FILE_UPLOAD_DIR;

    private final static List<Integer> defaultImageType = Arrays.asList(
            ImageType.MAIN.getValue(),
            ImageType.DEFAULT.getValue()
    );

    private final static List<Integer> defaultImageCoverType = Arrays.asList(
            ImageType.MAIN.getValue(),
            ImageType.THUMB.getValue()
    );

    @Transactional
    public SearchResult<Image> getEntityImages(ImageListQueryDTO param) {
        QueryWrapper<Image> wrapper = new QueryWrapper<Image>()
                .eq("entity_type", param.getEntityType())
                .eq("entity_id", param.getEntityId())
                .eq(param.getType() != null && param.getType() != -1 && param.getType() != -2, "type", param.getType())
                .in(param.getType() != null && param.getType() == -2, "type", defaultImageType)
                .orderBy(param.isSort(), param.asc(), CommonUtil.camelToUnderline(param.getSortField()));
        IPage<Image> pages = imageMapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);
        CommonImageUtil.generateThumb(pages.getRecords());
        return new SearchResult<>(pages.getRecords(), pages.getTotal());
    }

    @SneakyThrows
    @Transactional
    public void addEntityImage(int entityType, long entityId, List<ImageMiniDTO> images, boolean generateThumb) {
        //generate thumb
        if (generateThumb) {
            ImageMiniDTO cover = images.stream().filter(i -> i.getType() == ImageType.MAIN.getValue()).findFirst().orElse(null);
            if (cover != null) {
                ImageMiniDTO thumb = CommonImageUtil.generateThumb(cover);
                images.add(thumb);
            }
        }
        //upload to qiniu server
        List<Image> addImages = qiniuImageUtil.commonAddImages(entityType, entityId, images);
        //batch insert
        MybatisBatch.Method<Image> method = new MybatisBatch.Method<>(ImageMapper.class);
        MybatisBatch<Image> batchInsert = new MybatisBatch<>(sqlSessionFactory, addImages);
        batchInsert.execute(method.insert());

        //update item image redis cache
        if (entityType == EntityType.ITEM.getValue()) {
            resetEntityImageRedisCache(entityType, entityId);
        }
    }

    @Transactional
    public void updateEntityImage(Image image) {
        imageMapper.updateById(image);
    }

    @Transactional
    public void deleteEntityImage(List<Image> images) {
        //delete from qiniu server
        List<Image> deleteImages = qiniuImageUtil.deleteImage(images);
        //delete from database
        imageMapper.deleteByIds(deleteImages.stream().map(Image::getId).toList());
    }

    public String getEntityImageCache(int entityType, long entityId, ImageType imageType) {
        String url = redisUtil.get(
                STR."\{RedisKey.ENTITY_IMAGE_CACHE}\{imageType.getValue()}:\{entityType}:\{entityId}", String.class);
        return CommonImageUtil.getItemImage(imageType.getValue(), url);
    }

    public ImageDisplayVO getEntityDisplayImages(int entityType, long entityId) {
        QueryWrapper<Image> wrapper = new QueryWrapper<Image>()
                .eq("entity_type", entityType)
                .eq("entity_id", entityId)
                .in("type", defaultImageType)
                .orderByAsc("id");
        IPage<Image> pages = imageMapper.selectPage(new Page<>(1, 6), wrapper);
        CommonImageUtil.generateThumb(pages.getRecords());
        return new ImageDisplayVO(pages.getRecords(), imageMapper.selectCount(wrapper).intValue());
    }

    @SneakyThrows
    public void resetEntityImageRedisCache(int entityType, long entityId) {
        String key = STR."\{RedisKey.ENTITY_IMAGE_CACHE}%s:\{entityType}:\{entityId}";
        List<Image> images = imageMapper.selectList(
                new LambdaQueryWrapper<Image>()
                        .eq(Image::getEntityType, entityType)
                        .eq(Image::getEntityId, entityId)
                        .in(Image::getType, defaultImageCoverType)
        );
        String defaultCover = CommonConstant.EMPTY_IMAGE_URL;
        images.sort(DataSorter.imageEntityTypeEntityIdTypeSorter);
        Image cover = DataFinder.findImageByEntityTypeEntityIdType(entityType, entityId, ImageType.MAIN.getValue(), images);
        redisUtil.set(String.format(key, ImageType.MAIN.getValue()), cover != null ? cover.getUrl() : defaultCover);
        Image thumb = DataFinder.findImageByEntityTypeEntityIdType(entityType, entityId, ImageType.THUMB.getValue(), images);
        redisUtil.set(String.format(key, ImageType.THUMB.getValue()), thumb != null ? thumb.getUrl() : defaultCover);
    }

    // @SneakyThrows
    // @Transactional
    // public void uploadFileInfo(int entityType, long entityId, File file) {
    //     String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern(DateHelper.DATE_FORMAT));
    //     Path saveDir = Paths.get(FILE_UPLOAD_DIR, datePath);
    //     Files.createDirectories(saveDir);
    //
    //     String filename = file.getName();
    //     String newFileName = getNewFilename(filename);
    //
    //     Path destPath = saveDir.resolve(newFileName);
    //
    //     Files.copy(file.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
    //     file.delete();
    //
    //     FileInfo info = new FileInfo();
    //     info.setName(filename);
    //     info.setMime(tika.detect(file));
    //     info.setSize(file.length());
    //     info.setPath(STR."/upload/\{datePath}/\{newFileName}");
    //     fileInfoMapper.insert(info);
    //
    //     FileRelated related = new FileRelated();
    //     related.setEntityType(entityType);
    //     related.setEntityId(entityId);
    //     related.setFileId(info.getId());
    //     fileRelatedMapper.insert(related);
    // }

    @SneakyThrows
    public FileRelated generateFileRelated(int entityType, long entityId, File file) {
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern(DateHelper.DATE_FORMAT));
        Path saveDir = Paths.get(FILE_UPLOAD_DIR, datePath);

        Files.createDirectories(saveDir);
        String filename = file.getName();
        String newFileName = FileUtil.getNewFilename(filename);
        Path destPath = saveDir.resolve(newFileName);
        Files.copy(file.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);

        FileInfo info = new FileInfo();
        info.setName(filename);
        info.setMime(tika.detect(file));
        info.setSize(file.length());
        info.setPath(STR."/upload/\{datePath}/\{newFileName}");

        FileRelated related = new FileRelated();
        related.setEntityType(entityType);
        related.setEntityId(entityId);
        related.setFileInfo(info);

        return related;
    }

}
