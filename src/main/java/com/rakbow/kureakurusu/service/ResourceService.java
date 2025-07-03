package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.rakbow.kureakurusu.dao.FileInfoMapper;
import com.rakbow.kureakurusu.dao.FileRelatedMapper;
import com.rakbow.kureakurusu.dao.ImageMapper;
import com.rakbow.kureakurusu.data.CommonConstant;
import com.rakbow.kureakurusu.data.RedisKey;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.common.Constant;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.emun.ImageType;
import com.rakbow.kureakurusu.data.entity.resource.FileInfo;
import com.rakbow.kureakurusu.data.entity.resource.FileRelated;
import com.rakbow.kureakurusu.data.entity.resource.Image;
import com.rakbow.kureakurusu.data.vo.EntityRelatedCount;
import com.rakbow.kureakurusu.data.vo.resource.FileListVO;
import com.rakbow.kureakurusu.data.vo.resource.ImageDisplayVO;
import com.rakbow.kureakurusu.data.vo.resource.ImageVO;
import com.rakbow.kureakurusu.toolkit.*;
import com.rakbow.kureakurusu.toolkit.file.CommonImageUtil;
import com.rakbow.kureakurusu.toolkit.file.QiniuImageUtil;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Rakbow
 * @since 2024/5/26 20:10
 */
@Service
@RequiredArgsConstructor
public class ResourceService {

    private final ImageMapper imageMapper;
    private final FileInfoMapper fileMapper;
    private final FileRelatedMapper fileRelatedMapper;
    private final QiniuImageUtil qiniuImageUtil;
    private final SqlSessionFactory sqlSessionFactory;
    private final RedisUtil redisUtil;
    private final Converter converter;

    @Value("${system.path.upload.file}")
    private String FILE_UPLOAD_DIR;
    private final static String FILE_UPLOAD_PREFIX = "upload/file/";

    private final static List<Integer> defaultImageType = Arrays.asList(
            ImageType.MAIN.getValue(),
            ImageType.DEFAULT.getValue()
    );

    private final static List<Integer> defaultImageCoverType = Arrays.asList(
            ImageType.MAIN.getValue(),
            ImageType.THUMB.getValue()
    );

    @Transactional
    public SearchResult<ImageVO> getEntityImages(ImageListQueryDTO param) {
        MPJLambdaWrapper<Image> wrapper = new MPJLambdaWrapper<Image>()
                .eq(Image::getEntityType, param.getEntityType())
                .eq(Image::getEntityId, param.getEntityId())
                .eq(ObjectUtils.isNotEmpty(param.getType()) && param.getType() != -1 && param.getType() != -2, Image::getType, param.getType())
                .in(ObjectUtils.isNotEmpty(param.getType()) && param.getType() == -2, Image::getType, defaultImageType)
                .orderBy(param.isSort(), param.asc(), CommonUtil.camelToUnderline(param.getSortField()));
        IPage<Image> pages = imageMapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);
        List<ImageVO> res = converter.convert(pages.getRecords(), ImageVO.class);
        return new SearchResult<>(res, pages.getTotal());
    }

    @SneakyThrows
    @Transactional
    public void uploadEntityImage(int entityType, long entityId, List<ImageMiniDTO> images, boolean generateThumb) {
        //generate thumb
        if (generateThumb) {
            ImageMiniDTO cover = images.stream().filter(i -> i.getType() == ImageType.MAIN.getValue()).findFirst().orElse(null);
            if (cover != null) {
                ImageMiniDTO thumb = CommonImageUtil.generateThumbTmp(cover);
                images.add(thumb);
            }
        }
        //upload to qiniu server
        List<Image> addImages = qiniuImageUtil.uploadImages(entityType, entityId, images);
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
    public void deleteEntityImage(List<ImageDeleteDTO> images) {
        String[] keys = images.stream().map(ImageDeleteDTO::getUrl)
                .map(url -> url.replace(Constant.FILE_DOMAIN, "")).toArray(String[]::new);
        //delete from qiniu server
        List<Integer> deleteIndexes = qiniuImageUtil.deleteImages(keys);
        //delete from database
        imageMapper.deleteByIds(deleteIndexes.stream().map(index -> images.get(index).getId()).toList());
    }

    public String getEntityImageCache(int entityType, long entityId, ImageType imageType) {
        String url = redisUtil.get(
                STR."\{RedisKey.ENTITY_IMAGE_CACHE}\{imageType.getValue()}:\{entityType}:\{entityId}", String.class);
        return CommonImageUtil.getItemImage(imageType.getValue(), url);
    }

    public ImageDisplayVO getEntityDisplayImages(int entityType, long entityId) {
        IPage<Image> pages = imageMapper.selectPage(new Page<>(1, 6),
                new LambdaQueryWrapper<Image>().eq(Image::getEntityType, entityType)
                        .eq(Image::getEntityId, entityId)
                        .in(Image::getType, defaultImageType)
                        .orderByAsc(Image::getId)
        );
        return new ImageDisplayVO(converter.convert(pages.getRecords(), ImageVO.class), pages.getTotal());
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
        Image cover = images.stream().filter(i -> i.getType() == ImageType.MAIN).findFirst().orElse(null);
        redisUtil.set(String.format(key, ImageType.MAIN.getValue()), cover != null ? cover.getUrl() : defaultCover);
        Image thumb = images.stream().filter(i -> i.getType() == ImageType.THUMB).findFirst().orElse(null);
        redisUtil.set(String.format(key, ImageType.THUMB.getValue()), thumb != null ? thumb.getUrl() : defaultCover);
    }

    //region file

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
        info.setExtension(FileUtil.getExtension(filename));
        info.setSize(file.length());
        info.setPath(STR."/upload/\{datePath}/\{newFileName}");

        file.delete();

        FileRelated related = new FileRelated();
        related.setEntityType(entityType);
        related.setEntityId(entityId);
        related.setFileInfo(info);

        return related;
    }

    @Transactional
    @SneakyThrows
    public SearchResult<FileListVO> getFileList(ListQuery dto) {
        FileListQueryDTO param = new FileListQueryDTO(dto);
        Wrapper<FileInfo> wrapper;
        if (param.getEntityType() == null && param.getEntityId() == null) {
            wrapper = new QueryWrapper<FileInfo>()
                    .like(StringUtils.isNotEmpty(param.getName()), "name", param.getName())
                    .orderBy(param.isSort(), param.asc(), CommonUtil.camelToUnderline(param.getSortField()));
        } else {
            wrapper = new MPJLambdaWrapper<FileInfo>()
                    .selectAll(FileInfo.class)
                    .innerJoin(FileRelated.class, FileRelated::getFileId, FileInfo::getId)
                    .eq(FileRelated::getEntityType, param.getEntityType())
                    .eq(FileRelated::getEntityId, param.getEntityId())
                    .like(StringUtils.isNotEmpty(param.getName()), "name", param.getName())
                    .orderBy(param.isSort(), param.asc(), CommonUtil.camelToUnderline(param.getSortField()));
        }


        IPage<FileInfo> pages = fileMapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);
        List<FileListVO> res = converter.convert(pages.getRecords(), FileListVO.class);

        return new SearchResult<>(res, pages.getTotal(), pages.getCurrent(), pages.getSize());
    }

    @Transactional
    @SneakyThrows
    public void updateFile(FileUpdateDTO dto) {
        FileInfo file = converter.convert(dto, FileInfo.class);
        fileMapper.updateById(file);
    }

    @Transactional
    @SneakyThrows
    public void uploadFiles(int entityType, long entityId, MultipartFile[] files,
                              List<String> names, List<String> remarks) {
        List<FileRelated> addFileRelatedList = new ArrayList<>();
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern(DateHelper.DATE_FORMAT));
        Path saveDir = Paths.get(FILE_UPLOAD_DIR, datePath);
        Files.createDirectories(saveDir);

        int idx = 0;

        for (MultipartFile f : files) {
            File file = FileUtil.convertToTempFile(f);

            String newFileName = FileUtil.getNewFilename(file.getName());
            Path destPath = saveDir.resolve(newFileName);
            Files.copy(file.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);

            FileInfo info = new FileInfo();
            info.setName(names.get(idx));
            info.setExtension(FileUtil.getExtension(info.getName()));
            info.setSize(file.length());
            info.setPath(STR."\{FILE_UPLOAD_PREFIX}\{datePath}/\{newFileName}");
            info.setRemark(remarks.get(idx));

            FileRelated related = new FileRelated();
            related.setEntityType(entityType);
            related.setEntityId(entityId);
            related.setFileInfo(info);

            idx++;
            fileMapper.insert(info);
            file.delete();
            addFileRelatedList.add(related);
        }
        MybatisBatch.Method<FileRelated> fileRelatedMethod = new MybatisBatch.Method<>(FileRelatedMapper.class);
        MybatisBatch<FileRelated> frBatchInsert = new MybatisBatch<>(sqlSessionFactory, addFileRelatedList);
        addFileRelatedList.forEach(r -> r.setFileId(r.getFileInfo().getId()));
        frBatchInsert.execute(fileRelatedMethod.insert());
    }

    @Transactional
    @SneakyThrows
    public void createFileRelated(int entityType, long entityId, List<Long> fileIds) {
        List<FileRelated> addFileRelatedList = new ArrayList<>();
        fileIds.forEach(fid -> {
            FileRelated related = new FileRelated();
            related.setEntityType(entityType);
            related.setEntityId(entityId);
            related.setFileId(fid);
            addFileRelatedList.add(related);
        });
        MybatisBatch.Method<FileRelated> fileRelatedMethod = new MybatisBatch.Method<>(FileRelatedMapper.class);
        MybatisBatch<FileRelated> frBatchInsert = new MybatisBatch<>(sqlSessionFactory, addFileRelatedList);
        frBatchInsert.execute(fileRelatedMethod.insert());
    }

    @Transactional
    @SneakyThrows
    public SearchResult<FileListVO> searchFiles(FileSearchParams param) {
        // 记录开始时间
        long start = System.currentTimeMillis();
        LambdaQueryWrapper<FileInfo> wrapper = new LambdaQueryWrapper<FileInfo>().eq(FileInfo::getStatus, 1);
        if (!param.getKeywords().isEmpty()) param.getKeywords().forEach(k -> wrapper.like(FileInfo::getName, k));
        IPage<FileInfo> pages = fileMapper.selectPage(new Page<>(param.getPage(), param.getSize()), wrapper);
        List<FileListVO> res = converter.convert(pages.getRecords(), FileListVO.class);
        return new SearchResult<>(res, pages.getTotal(), pages.getCurrent(), pages.getSize(),
                String.format("%.2f", (System.currentTimeMillis() - start) / 1000.0));
    }

    public List<EntityRelatedCount> getFileCount(Integer entityType, List<Long> ids) {
        QueryWrapper<FileRelated> wrapper = new QueryWrapper<FileRelated>()
                .select("entity_id", "COUNT(file_id) AS count")
                .eq("entity_type", entityType)
                .in("entity_id", ids)
                .groupBy("entity_type", "entity_id");
        List<Map<String, Object>> maps = fileRelatedMapper.selectMaps(wrapper);

        List<EntityRelatedCount> res = JsonUtil.to(maps, EntityRelatedCount.class);
        res.forEach(r -> r.setEntityType(entityType));
        return res;
    }

    public List<EntityRelatedCount> getImageCount(Integer entityType, List<Long> ids) {
        QueryWrapper<Image> wrapper = new QueryWrapper<Image>()
                .select("entity_id", "COUNT(id) AS count")
                .eq("entity_type", entityType)
                .eq("type", ImageType.DEFAULT)
                .in("entity_id", ids)
                .groupBy("entity_type", "entity_id");
        List<Map<String, Object>> maps = imageMapper.selectMaps(wrapper);

        List<EntityRelatedCount> res = JsonUtil.to(maps, EntityRelatedCount.class);
        res.forEach(r -> r.setEntityType(entityType));
        return res;
    }

    //endregion

}
