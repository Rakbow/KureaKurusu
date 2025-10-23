package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.rakbow.kureakurusu.dao.FileInfoMapper;
import com.rakbow.kureakurusu.dao.FileRelatedMapper;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.emun.ChangelogField;
import com.rakbow.kureakurusu.data.emun.ChangelogOperate;
import com.rakbow.kureakurusu.data.entity.resource.FileInfo;
import com.rakbow.kureakurusu.data.entity.resource.FileRelated;
import com.rakbow.kureakurusu.data.vo.EntityRelatedCount;
import com.rakbow.kureakurusu.data.vo.resource.FileListVO;
import com.rakbow.kureakurusu.data.vo.resource.FileRelatedVO;
import com.rakbow.kureakurusu.toolkit.CommonUtil;
import com.rakbow.kureakurusu.toolkit.DateHelper;
import com.rakbow.kureakurusu.toolkit.FileUtil;
import com.rakbow.kureakurusu.toolkit.JsonUtil;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
import java.util.List;
import java.util.Map;

/**
 * @author Rakbow
 * @since 2025/7/10 3:41
 */
@Service
@RequiredArgsConstructor
public class FileService extends ServiceImpl<FileInfoMapper, FileInfo> {

    private final FileRelatedMapper fileRelatedMapper;
    private final Converter converter;
    private final SqlSessionFactory sqlSessionFactory;

    private final ChangelogService logSrv;

    @Value("${system.path.upload.file}")
    private String FILE_UPLOAD_DIR;
    private final static String FILE_UPLOAD_PREFIX = "upload/file/";

    @Transactional
    @SneakyThrows
    public SearchResult<FileListVO> search(FileSearchParams param) {
        LambdaQueryWrapper<FileInfo> wrapper = new LambdaQueryWrapper<FileInfo>()
                .eq(FileInfo::getStatus, 1)
                .orderByDesc(FileInfo::getId);
        if (!param.getKeywords().isEmpty()) param.getKeywords().forEach(k -> wrapper.like(FileInfo::getName, k));
        IPage<FileInfo> pages = page(new Page<>(param.getPage(), param.getSize()), wrapper);
        List<FileListVO> res = converter.convert(pages.getRecords(), FileListVO.class);
        return new SearchResult<>(res, pages.getTotal());
    }

    @Transactional
    @SneakyThrows
    public SearchResult<FileListVO> list(FileListQueryDTO dto) {
        MPJLambdaWrapper<FileInfo> wrapper = new MPJLambdaWrapper<FileInfo>()
                .selectAll(FileInfo.class)
                .like(StringUtils.isNotEmpty(dto.getKeyword()), FileInfo::getName, dto.getKeyword())
                .orderBy(dto.isSort(), dto.asc(), dto.getSortField())
                .orderByDesc(!dto.isSort(), FileInfo::getId);

        //get file related part
        if (dto.getEntityType() != null && dto.getEntityId() != null) {
            wrapper.selectAs(FileRelated::getId, FileInfo::getRelatedId)
                    .innerJoin(FileRelated.class, FileRelated::getFileId, FileInfo::getId)
                    .eq(FileRelated::getEntityType, dto.getEntityType())
                    .eq(FileRelated::getEntityId, dto.getEntityId());
        }
        IPage<FileInfo> pages = page(new Page<>(dto.getPage(), dto.getSize()), wrapper);
        List<FileListVO> res = converter.convert(pages.getRecords(), FileListVO.class);

        return new SearchResult<>(res, pages.getTotal());
    }

    @Transactional
    @SneakyThrows
    public void upload(int entityType, long entityId, MultipartFile[] files, List<String> names) {
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

            FileRelated related = new FileRelated();
            related.setEntityType(entityType);
            related.setEntityId(entityId);
            related.setFileInfo(info);

            idx++;
            save(info);
            file.delete();
            addFileRelatedList.add(related);
        }
        MybatisBatch.Method<FileRelated> fileRelatedMethod = new MybatisBatch.Method<>(FileRelatedMapper.class);
        MybatisBatch<FileRelated> frBatchInsert = new MybatisBatch<>(sqlSessionFactory, addFileRelatedList);
        addFileRelatedList.forEach(r -> r.setFileId(r.getFileInfo().getId()));
        frBatchInsert.execute(fileRelatedMethod.insert());

        logSrv.create(entityType, entityId, ChangelogField.FILE, ChangelogOperate.UPLOAD);
    }

    @Transactional
    @SneakyThrows
    public void update(FileUpdateDTO dto) {
        FileInfo file = converter.convert(dto, FileInfo.class);
        updateById(file);
    }

    @Transactional
    @SneakyThrows
    public void createRelated(int entityType, long entityId, List<Long> fileIds) {
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

        logSrv.create(entityType, entityId, ChangelogField.FILE, ChangelogOperate.UPDATE);
    }

    @Transactional
    @SneakyThrows
    public void deleteRelated(FileRelatedDeleteDTO dto) {
        fileRelatedMapper.deleteByIds(dto.getIds());
        logSrv.create(dto.getEntityType(), dto.getEntityId(), ChangelogField.FILE, ChangelogOperate.DELETE);
    }

    @SneakyThrows
    public FileRelated generateRelated(int entityType, long entityId, File file) {
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
    public FileRelatedVO related(EntityQryDTO dto) {
        MPJLambdaWrapper<FileInfo> wrapper = new MPJLambdaWrapper<FileInfo>()
                .selectAll(FileInfo.class)
                .innerJoin(FileRelated.class, FileRelated::getFileId, FileInfo::getId)
                .eq(FileRelated::getEntityType, dto.getEntityType())
                .eq(FileRelated::getEntityId, dto.getEntityId())
                .orderByDesc(FileInfo::getId);

        List<FileInfo> files = list(wrapper);
        if(files.isEmpty()) new FileRelatedVO();

        List<FileListVO> fileVOs = converter.convert(files, FileListVO.class);
        long bytes = files.stream().mapToLong(FileInfo::getSize).sum();

        return new FileRelatedVO(fileVOs, CommonUtil.getFileSize(bytes));
    }

    public List<EntityRelatedCount> count(Integer entityType, List<Long> ids) {
        MPJLambdaWrapper<FileRelated> wrapper = new MPJLambdaWrapper<FileRelated>()
                .select(FileRelated::getEntityId)
                .selectCount(FileRelated::getFileId, "count")
                .eq(FileRelated::getEntityType, entityType)
                .in(FileRelated::getEntityId, ids)
                .groupBy(FileRelated::getEntityType, FileRelated::getEntityId);
        List<Map<String, Object>> maps = fileRelatedMapper.selectMaps(wrapper);

        List<EntityRelatedCount> res = JsonUtil.to(maps, EntityRelatedCount.class);
        res.forEach(r -> r.setEntityType(entityType));
        return res;
    }

}
