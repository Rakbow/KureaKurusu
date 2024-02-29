package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.EpisodeMapper;
import com.rakbow.kureakurusu.data.emun.common.Entity;
import com.rakbow.kureakurusu.data.emun.system.FileType;
import com.rakbow.kureakurusu.data.entity.Episode;
import com.rakbow.kureakurusu.data.system.ActionResult;
import com.rakbow.kureakurusu.data.system.File;
import com.rakbow.kureakurusu.util.common.DateHelper;
import com.rakbow.kureakurusu.util.file.QiniuBaseUtil;
import com.rakbow.kureakurusu.util.file.QiniuFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Rakbow
 * @since 2024/01/08 16:38
 */
@Service
@RequiredArgsConstructor
public class EpisodeService extends ServiceImpl<EpisodeMapper, Episode> {

    private final EpisodeMapper mapper;
    private final QiniuFileUtil qiniuFileUtil;
    private final QiniuBaseUtil qiniuBaseUtil;

    @SneakyThrows
    @Transactional
    public void updateFile(int id, MultipartFile[] files, List<File> fileInfos) {

        //org files
        List<File> orgFiles = mapper.selectById(id).getFiles();
        //new files
        List<File> finalFiles = new ArrayList<>();

        //创建存储链接前缀
        String filePath = "file/" + Entity.EPISODE.getTableName() + "/" + id + "/";

        for (int i = 0; i < files.length; i++) {
            ActionResult ar;
            //判断文件是否为lrc歌词
            if (StringUtils.equals(Objects.requireNonNull(files[i].getOriginalFilename()).substring(Objects.requireNonNull(files[i].getOriginalFilename()).lastIndexOf(".")+1), "lrc")) {
                //上传lrc歌词文件
                ar = qiniuBaseUtil.uploadFileToQiniu(files[i], filePath, FileType.TEXT);
            }else {
                //上传音频文件
                ar = qiniuBaseUtil.uploadFileToQiniu(files[i], filePath, FileType.AUDIO);
            }
            if (ar.state) {
                File file = File.builder()
                        .url(ar.data.toString())
                        .name(fileInfos.get(i).getName())
                        .size(fileInfos.get(i).getSize())
                        .type(fileInfos.get(i).getType())
                        .uploadTime(DateHelper.nowStr())
//                        .uploadUser(user.getUsername())
                        .build();
                finalFiles.add(file);
            }
        }
        orgFiles.addAll(finalFiles);
        update(
                new LambdaUpdateWrapper<Episode>()
                        .eq(Episode::getId, id)
                        .set(Episode::getFiles, finalFiles)
                        .set(Episode::getEditedTime, DateHelper.now())
        );
    }

    @SneakyThrows
    @Transactional
    public void deleteFiles(long id, List<File> delFiles) {
        //original files
        List<File> orgFiles = mapper.selectById(id).getFiles();
        List<File> finalFiles = qiniuFileUtil.deleteFile(orgFiles, delFiles);
        update(
                new LambdaUpdateWrapper<Episode>()
                        .eq(Episode::getId, id)
                        .set(Episode::getFiles, finalFiles)
                        .set(Episode::getEditedTime, DateHelper.now())
        );
    }

}
