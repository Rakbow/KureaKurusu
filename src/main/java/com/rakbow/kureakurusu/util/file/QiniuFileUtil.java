package com.rakbow.kureakurusu.util.file;

import com.rakbow.kureakurusu.data.system.ActionResult;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.data.system.File;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2023-02-03 0:14
 */
@RequiredArgsConstructor
@Component
public class QiniuFileUtil {

    private final QiniuBaseUtil qiniuBaseUtil;

    @SuppressWarnings("unchecked")
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public List<Image> deleteImage(List<Image> images, List<Image> deleteImages) throws Exception {

        //从七牛云上删除
        //删除结果
        List<String> deleteResult = new ArrayList<>();
        //若删除的文件只有一个，调用单张删除方法
        if (deleteImages.size() == 1) {
            ActionResult ar = qiniuBaseUtil.deleteFileFromQiniu(deleteImages.get(0).getUrl());
            if (ar.fail())
                throw new Exception(ar.message);
            deleteResult.add(deleteImages.get(0).getUrl());
        } else {
            String[] fullFileUrlList = new String[deleteImages.size()];
            for (int i = 0; i < deleteImages.size(); i++) {
                fullFileUrlList[i] = deleteImages.get(i).getUrl();
            }
            ActionResult ar = qiniuBaseUtil.deleteFilesFromQiniu(fullFileUrlList);
            deleteResult = (List<String>) ar.data;
        }

        //根据删除结果循环删除文件信息json数组
        // 迭代器

        for (String s : deleteResult) {
            // 删除数组元素
            images.removeIf(image -> StringUtils.equals(image.getUrl(), s));
        }

        return images;
    }

    @SuppressWarnings("unchecked")
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public List<File> deleteFile(List<File> files, List<File> deleteFiles) throws Exception {

        //从七牛云上删除
        //删除结果
        List<String> deleteResult = new ArrayList<>();
        //若删除的文件只有一个，调用单张删除方法
        if (deleteFiles.size() == 1) {
            ActionResult ar = qiniuBaseUtil.deleteFileFromQiniu(deleteFiles.get(0).getUrl());
            if (ar.fail())
                throw new Exception(ar.message);
            deleteResult.add(deleteFiles.get(0).getUrl());
        } else {
            String[] fullFileUrlList = new String[deleteFiles.size()];
            for (int i = 0; i < deleteFiles.size(); i++) {
                fullFileUrlList[i] = deleteFiles.get(i).getUrl();
            }
            ActionResult ar = qiniuBaseUtil.deleteFilesFromQiniu(fullFileUrlList);
            deleteResult = (List<String>) ar.data;
        }

        //根据删除结果循环删除文件信息json数组
        // 迭代器

        for (String s : deleteResult) {
            // 删除数组元素
            files.removeIf(file -> StringUtils.equals(file.getUrl(), s));
        }

        return files;
    }

    //region 全部删除

    /**
     * 通用删除所有图片
     *
     * @param images 删除图片合集
     * @author Rakbow
     */
    public void deleteAllImage(List<Image> images) {
        String[] deleteImageKeyList = new String[images.size()];
        for (int i = 0; i < images.size(); i++) {
            deleteImageKeyList[i] = images.get(i).getUrl();
        }
        qiniuBaseUtil.deleteFilesFromQiniu(deleteImageKeyList);
    }

    /**
     * 通用删除所有文件
     *
     * @param files 删除文件合集
     * @author Rakbow
     */
    public void deleteAllFile(List<File> files) {
        String[] deleteFileKeyList = new String[files.size()];
        for (int i = 0; i < files.size(); i++) {
            deleteFileKeyList[i] = files.get(i).getUrl();
        }
        qiniuBaseUtil.deleteFilesFromQiniu(deleteFileKeyList);
    }

    //endregion

}
