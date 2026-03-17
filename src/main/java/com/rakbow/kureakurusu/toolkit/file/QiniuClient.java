package com.rakbow.kureakurusu.toolkit.file;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.util.Auth;
import com.rakbow.kureakurusu.data.common.ActionResult;
import com.rakbow.kureakurusu.exception.ApiException;
import com.rakbow.kureakurusu.exception.ErrorFactory;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

/**
 * 通过七牛云api实现文件的增删改查
 *
 * @author Rakbow
 * @since 2022-12-01 1:44
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class QiniuClient {

    @Value("${qiniu.bucketName}")
    private String BUCKET_NAME;
    private final UploadManager uploadManager;
    private final Auth auth;
    private final BucketManager bucketManager;


    /**
     * 获取上传文件的token值
     */
    public String getUpToken() {
        // 密钥配置
        return auth.uploadToken(BUCKET_NAME);
    }

    /**
     * 统一上传文件方法，单个
     *
     * @param file,filePath,fileExt 文件，路径，文件类型
     * @return ActionResult
     */
    @SneakyThrows
    public ActionResult upload(byte[] file, String fileExt, String filePath) {
        ActionResult ar = new ActionResult();
        try {
            // 通过随机UUID生成唯一文件名 长度：16
            String fileName = STR."\{UUID.randomUUID().toString().replaceAll("-", "")}.\{fileExt}";
            String fullFileName = filePath + fileName;
            // 调用put方法上传
            Response res = uploadManager.put(file, fullFileName, getUpToken());

            // 打印返回的信息
            if (res.isOK() && res.isJson()) {
                // 返回存储文件的地址
                ar.data = fullFileName;
            } else {
                ar.setErrorMessage(I18nHelper.getMessage("qiniu.exception", res.bodyString()));
            }
            return ar;
        } catch (QiniuException ex) {
            // 请求失败时打印的异常的信息
            ar.setErrorMessage(I18nHelper.getMessage("qiniu.exception", ex.getMessage()));
            return ar;
        }
    }

    @SneakyThrows
    public List<Integer> delete(String[] keys) {
        List<Integer> deleteIndexes = new ArrayList<>();
        try {
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            //batch delete
            batchOperations.addDeleteOp(BUCKET_NAME, keys);
            //filter successfully deleted indexes
            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            IntStream.range(0, keys.length).filter(i -> batchStatusList[i].code == HttpStatus.OK.value())
                    .forEach(deleteIndexes::add);
        } catch (QiniuException ex) {
            throw ErrorFactory.qiniuError(ex);
        }
        return deleteIndexes;
    }

    @SneakyThrows
    public void delete(String key) {
        try {
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            //batch delete
            batchOperations.addDeleteOp(BUCKET_NAME, key);
            //filter successfully deleted indexes
            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            if(batchStatusList[0].code != HttpStatus.OK.value()) {
                throw new ApiException("qiniu.exception", (batchStatusList[0].data.error));
            }
        } catch (QiniuException ex) {
            throw ErrorFactory.qiniuError(ex);
        }
    }

}
