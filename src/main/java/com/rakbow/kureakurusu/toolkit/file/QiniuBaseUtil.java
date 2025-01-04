package com.rakbow.kureakurusu.toolkit.file;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.util.Auth;
import com.rakbow.kureakurusu.data.emun.FileType;
import com.rakbow.kureakurusu.data.common.ActionResult;
import com.rakbow.kureakurusu.toolkit.I18nHelper;
import com.rakbow.kureakurusu.toolkit.FileUtil;
import com.rakbow.kureakurusu.toolkit.JsonUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * 通过七牛云api实现文件的增删改查
 *
 * @author Rakbow
 * @since 2022-12-01 1:44
 */
@Component
public class QiniuBaseUtil {

    @Value("${kureakurusu.qiniu.access-key}")
    private String ACCESS_KEY;
    @Value("${kureakurusu.qiniu.secret-key}")
    private String SECRET_KEY;
    @Value("${kureakurusu.qiniu.image.domain}")
    private String FILE_DOMAIN;
    @Value("${kureakurusu.qiniu.bucketName}")
    private String BUCKET_NAME;
    

    /**
     * 获取上传文件的token值
     */
    public String getUpToken() {
        // 密钥配置
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        return auth.uploadToken(BUCKET_NAME);
    }

    /**
     * 统一上传文件方法，单个
     *
     * @param file,filePath,fileType 文件，路径，文件类型
     * @return ActionResult
     */
    @SneakyThrows
    public ActionResult uploadFileToQiniu(MultipartFile file, String filePath, FileType fileType) {
        ActionResult ar = new ActionResult();
        try {
            // 构造一个带指定Zone对象的配置类,不同的七云牛存储区域调用不同的zone
            // 华东-浙江2 CnEast2
            Configuration cfg = new Configuration(Region.autoRegion());
            // 创建上传对象
            UploadManager uploadManager = new UploadManager(cfg);

            // 检测文件是否为空
            if (file.isEmpty()) {
                ar.setErrorMessage(I18nHelper.getMessage("file.empty"));
                return ar;
            }

            // 检测文件格式是否合法
            int dotPos = Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf(".");
            if (dotPos < 0) {
                ar.setErrorMessage(I18nHelper.getMessage("file.format.error", fileType.getNameZh()));
                return ar;
            }

            // 获取文件后缀名
            String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
            // 检测格式是否支持
            if (FileUtil.isFileFormatAllowed(fileExt, fileType)) {
                return ar.fail(I18nHelper.getMessage("file.format.unsupported", fileType.getNameZh()));
            }

            // 通过随机UUID生成唯一文件名 长度：16
            String fileName = STR."\{UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8)}.\{fileExt}";

            // 生成完整文件名，例：album/11/xxx.jpg
            String fullFileName = filePath + fileName;

            // 调用put方法上传
            Response res = uploadManager.put(file.getBytes(), fullFileName, getUpToken());

            // 打印返回的信息
            if (res.isOK() && res.isJson()) {
                // 返回存储文件的地址
                HashMap<String, String> resDic = JsonUtil.toMap(res.bodyString(), String.class, String.class);
                ar.data = resDic.get("key");
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

    /**
     * 统一删除文件方法，单个
     *
     * @param fullFileName 需要删除文件的全路径文件名
     * @return ActionResult
     */
    public ActionResult deleteFileFromQiniu(String fullFileName){

        ActionResult ar = new ActionResult();
        try{
            //构造一个带指定Zone对象的配置类
            Configuration cfg = new Configuration(Region.autoRegion());
            Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
            BucketManager bucketManager = new BucketManager(auth, cfg);

            //去除前缀，获得文件的key
            String key = fullFileName.replace(FILE_DOMAIN, "");

            bucketManager.delete(BUCKET_NAME, key);
        }catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            ar.setErrorMessage(I18nHelper.getMessage("qiniu.exception", ex.response.toString()));
        }
        return ar;
    }

    /**
     * 统一删除文件方法，多个，同一实体类型
     *
     * @param fullFileNames 需要删除文件的全路径文件名数组
     * @return ActionResult
     */
    public ActionResult deleteFilesFromQiniu(String[] fullFileNames){

        ActionResult ar = new ActionResult();
        try{
            // 删除结果map，key为文件名，value为删除状态，true为删除成功，false为删除失败
            List<String> deleteResults = new ArrayList<>();

            //对删除url数组进行处理，去除前缀获得key
            String[] keyList = new String[fullFileNames.length];
            for (int i = 0; i < fullFileNames.length; i++) {
                keyList[i] = fullFileNames[i].replace(FILE_DOMAIN, "");
            }

            //构造一个带指定Zone对象的配置类
            Configuration cfg = new Configuration(Region.autoRegion());
            Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
            BucketManager bucketManager = new BucketManager(auth, cfg);
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();

            batchOperations.addDeleteOp(BUCKET_NAME, keyList);

            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            for (int i = 0; i < fullFileNames.length; i++) {
                BatchStatus status = batchStatusList[i];
                if (status.code == 200) {
                    deleteResults.add(fullFileNames[i]);
                }
            }
            ar.data = deleteResults;
        }catch (QiniuException ex) {
            //如果遇到异常，说明删除失败
            ar.setErrorMessage(I18nHelper.getMessage("qiniu.exception", ex.response.toString()));
        }
        return ar;
    }

    /**
     * 统一上传文件方法，单个
     *
     * @param file,filePath,fileType 文件，路径，文件类型
     * @return ActionResult
     */
    @SneakyThrows
    public ActionResult uploadFileToQiniu(byte[] file, String fileExt, String filePath, FileType fileType) {
        ActionResult ar = new ActionResult();
        try {
            // 构造一个带指定Zone对象的配置类,不同的七云牛存储区域调用不同的zone
            // 华东-浙江2 CnEast2
            Configuration cfg = new Configuration(Region.autoRegion());
            // 创建上传对象
            UploadManager uploadManager = new UploadManager(cfg);

            // 检测文件是否为空
            if (file.length == 0) {
                ar.setErrorMessage(I18nHelper.getMessage("file.empty"));
                return ar;
            }

            // 检测格式是否支持
            if (FileUtil.isFileFormatAllowed(fileExt, fileType)) {
                return ar.fail(I18nHelper.getMessage("file.format.unsupported", fileType.getNameZh()));
            }

            // 通过随机UUID生成唯一文件名 长度：16
            String fileName = STR."\{UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8)}.\{fileExt}";

            // 生成完整文件名，例：album/11/xxx.jpg
            String fullFileName = filePath + fileName;

            // 调用put方法上传
            Response res = uploadManager.put(file, fullFileName, getUpToken());

            // 打印返回的信息
            if (res.isOK() && res.isJson()) {
                // 返回存储文件的地址
                HashMap<String, String> resDic = JsonUtil.toMap(res.bodyString(), String.class, String.class);
                ar.data = resDic.get("key");
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

}
