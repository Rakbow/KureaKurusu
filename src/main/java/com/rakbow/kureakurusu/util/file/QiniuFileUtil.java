package com.rakbow.kureakurusu.util.file;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.ActionResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Rakbow
 * @since 2023-02-03 0:14
 */
@Component
public class QiniuFileUtil {

    private final QiniuBaseUtil qiniuBaseUtil;

    public QiniuFileUtil(QiniuBaseUtil qiniuBaseUtil) {
        this.qiniuBaseUtil = qiniuBaseUtil;
    }

    /**
     * 通用删除文件
     *
     * @param files       原始文件json数组
     * @param deleteFiles 需要删除的文件jsonArray
     * @return files 最终保存到数据库的json信息
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public JSONArray commonDeleteFiles(JSONArray files, JSONArray deleteFiles) throws Exception {

        //从七牛云上删除
        //删除结果
        List<String> deleteResult = new ArrayList<>();
        //若删除的文件只有一个，调用单张删除方法
        if (deleteFiles.size() == 1) {
            ActionResult ar = qiniuBaseUtil.deleteFileFromQiniu
                    (deleteFiles.getJSONObject(0).getString("url"));
            if (!ar.state) {
                throw new Exception(ar.message);
            }
            deleteResult.add(deleteFiles.getJSONObject(0).getString("url"));
        } else {
            String[] fullFileUrlList = new String[deleteFiles.size()];
            for (int i = 0; i < deleteFiles.size(); i++) {
                fullFileUrlList[i] = deleteFiles.getJSONObject(i).getString("url");
            }
            ActionResult ar = qiniuBaseUtil.deleteFilesFromQiniu(fullFileUrlList);
            deleteResult = (List<String>) ar.data;
        }

        //根据删除结果循环删除文件信息json数组
        // 迭代器

        for (String s : deleteResult) {
            Iterator<Object> iterator = files.iterator();
            while (iterator.hasNext()) {
                JSONObject itJson = (JSONObject) iterator.next();
                if (StringUtils.equals(itJson.getString("url"), s)) {
                    // 删除数组元素
                    iterator.remove();
                }
            }
        }

        return files;
    }

    /**
     * 通用删除所有文件
     *
     * @param files 删除文件合集
     * @author rakbow
     */
    public void commonDeleteAllFiles(JSONArray files) {

        String[] deleteFileKeyList = new String[files.size()];
        //文件名
        String deleteFileUrl;
        for (int i = 0; i < files.size(); i++) {
            JSONObject file = files.getJSONObject(i);
            deleteFileUrl = file.getString("url");
            //删除七牛服务器上对应图片文件
            deleteFileKeyList[i] = deleteFileUrl;
        }
        qiniuBaseUtil.deleteFilesFromQiniu(deleteFileKeyList);
    }

}
