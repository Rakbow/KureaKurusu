package com.rakbow.kureakurusu.toolkit.file;

import com.rakbow.kureakurusu.data.common.ActionResult;
import com.rakbow.kureakurusu.data.CommonConstant;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.data.image.TempImage;
import com.rakbow.kureakurusu.data.emun.FileType;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2022-12-01 21:25
 */
@Component
@RequiredArgsConstructor
public class QiniuImageUtil {

    private final QiniuBaseUtil qiniuBaseUtil;
    private final static String THUMBNAIL_URL = "?imageMogr2/auto-orient/thumbnail/";
    @Value("${kureakurusu.qiniu.image.domain}")
    private static String FILE_DOMAIN;

    /**
     * 通用新增图片
     *
     * @param entityId                 实体id
     * @param entityName               实体表名
     * @param files             新增图片文件数组
     * @param addImages         新增图片json数据
     * @return finalImageJson 最终保存到数据库的json信息
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public ActionResult commonAddImages(long entityId, String entityName, MultipartFile[] files, List<Image> addImages) {
        ActionResult res = new ActionResult();
        try{
            //创建存储链接前缀
            String filePath = STR."\{entityName}/\{entityId}/";
            for (int i = 0; i < files.length; i++) {
                //上传图片
                ActionResult ar = qiniuBaseUtil.uploadFileToQiniu(files[i], filePath, FileType.IMAGE);
                if (!ar.state) throw new Exception(ar.message);
                Image image = addImages.get(i);
                image.setUrl(ar.data.toString());
            }
        }catch(Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return res;
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public List<TempImage> deleteImage(List<TempImage> images, List<TempImage> deleteImages) {

        //从七牛云上删除
        //删除结果
        List<String> deleteResult = new ArrayList<>();
        //若删除的文件只有一个，调用单张删除方法
        if (deleteImages.size() == 1) {
            ActionResult ar = qiniuBaseUtil.deleteFileFromQiniu(deleteImages.getFirst().getUrl());
            if (ar.fail())
                throw new Exception(ar.message);
            deleteResult.add(deleteImages.getFirst().getUrl());
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

    /**
     * delete all images
     *
     * @param images delete images
     * @author Rakbow
     */
    public void deleteAllImage(List<TempImage> images) {
        String[] deleteImageKeyList = new String[images.size()];
        for (int i = 0; i < images.size(); i++) {
            deleteImageKeyList[i] = images.get(i).getUrl();
        }
        qiniuBaseUtil.deleteFilesFromQiniu(deleteImageKeyList);
    }

    /**
     * 获取等比固定高宽的缩略图URL
     *
     * @param imageUrl,size 原始图url，缩略图宽高
     * @return thumbImageUrl
     */
    public static String getThumbUrl(String imageUrl, int size) {
        return STR."\{imageUrl}\{THUMBNAIL_URL}\{size}x\{size}";
    }

    public static String getCustomThumbUrl(String imageUrl, int size, int lengthLabel) {
        if(lengthLabel == 0) {
            return STR."\{imageUrl}\{THUMBNAIL_URL}\{size}x";
        }else {
            return STR."\{imageUrl}\{THUMBNAIL_URL}x\{size}";
        }
    }

    public static String getThumbUrlWidth(String imageUrl, int size) {
        return STR."\{imageUrl}\{THUMBNAIL_URL}200x\{size}";
    }

    public static String getThumbBackgroundUrl(String imageUrl, int size) {
        return STR."\{imageUrl}\{THUMBNAIL_URL}\{size}x\{size}/extent/\{size}x\{size}/background/IzJmMzY0Zg==";
    }

    public static String getBookThumbBackgroundUrl(String imageUrl, double width, double height) {

        return STR."\{imageUrl}\{THUMBNAIL_URL}\{width}x\{height}/extent/\{width}x\{height}/background/IzJmMzY0Zg==";
    }

    /**
     * 通过外链获取图片key
     *
     * @param fullImageUrl 原始图url
     * @return thumbImageUrl
     */
    public static String getImageKeyByFullUrl(String fullImageUrl) {
        return fullImageUrl.replace(FILE_DOMAIN, "");
    }

    public static String getThumb70Url(List<TempImage> images) {
        if (!images.isEmpty()) {
            for (TempImage image : images) {
                if(image.isMain())
                    return getThumbBackgroundUrl(image.getUrl(), 70);
            }
        }
        return getThumbUrl(CommonConstant.EMPTY_IMAGE_URL, 70);
    }

}
