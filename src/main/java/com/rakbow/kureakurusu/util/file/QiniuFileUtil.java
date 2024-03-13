package com.rakbow.kureakurusu.util.file;

import com.rakbow.kureakurusu.data.system.ActionResult;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.data.system.File;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
    @SneakyThrows
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public List<File> deleteFile(List<File> files, List<File> deleteFiles) {

        //从七牛云上删除
        //删除结果
        List<String> deleteResult = new ArrayList<>();
        //若删除的文件只有一个，调用单张删除方法
        if (deleteFiles.size() == 1) {
            ActionResult ar = qiniuBaseUtil.deleteFileFromQiniu(deleteFiles.getFirst().getUrl());
            if (ar.fail())
                throw new Exception(ar.message);
            deleteResult.add(deleteFiles.getFirst().getUrl());
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
