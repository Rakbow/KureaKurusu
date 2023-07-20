package com.rakbow.kureakurusu.util.file;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.ActionResult;
import com.rakbow.kureakurusu.data.CommonConstant;
import com.rakbow.kureakurusu.data.image.Image;
import com.rakbow.kureakurusu.data.emun.system.FileType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2022-12-01 21:25
 * @Description:
 */
@Component
public class QiniuImageUtil {

    private final QiniuBaseUtil qiniuBaseUtil;

    public QiniuImageUtil(QiniuBaseUtil qiniuBaseUtil) {
        this.qiniuBaseUtil = qiniuBaseUtil;
    }

    /**
     * 通用新增图片
     *
     * @param entityId                 实体id
     * @param entityName               实体表名
     * @param images             新增图片文件数组
     * @param originalImagesJson 数据库中现存的图片json数据
     * @param newImageInfos         新增图片json数据
     * @return finalImageJson 最终保存到数据库的json信息
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public ActionResult commonAddImages(int entityId, String entityName, MultipartFile[] images,
                                        List<Image> originalImagesJson, List<Image> newImageInfos) {
        ActionResult res = new ActionResult();
        try{

            //新增图片信息json
            List<Image> addImageJson = new ArrayList<>();

            //创建存储链接前缀
            String filePath = entityName + "/" + entityId + "/";

            for (int i = 0; i < images.length; i++) {
                //上传图片
                ActionResult ar = qiniuBaseUtil.uploadFileToQiniu(images[i], filePath, FileType.IMAGE);
                if (!ar.state) throw new Exception(ar.message);
                Image image = newImageInfos.get(i);
                image.setUrl(ar.data.toString());
                addImageJson.add(image);
            }

            //汇总
            originalImagesJson.addAll(addImageJson);

            res.data = originalImagesJson;
        }catch(Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return res;
    }

    /**
     * 获取等比固定高宽的缩略图URL
     *
     * @param imageUrl,size 原始图url，缩略图宽高
     * @return thumbImageUrl
     */
    public static String getThumbUrl(String imageUrl, int size) {
        return imageUrl + "?imageMogr2/auto-orient/thumbnail/" + size + "x" + size;
    }

    public static String getCustomThumbUrl(String imageUrl, int size, int lengthLabel) {
        if(lengthLabel == 0) {
            return imageUrl + "?imageMogr2/auto-orient/thumbnail/" + size + "x";
        }else {
            return imageUrl + "?imageMogr2/auto-orient/thumbnail/" + "x" + size;
        }
    }

    public static String getThumbUrlWidth(String imageUrl, int size) {
        return imageUrl + "?imageMogr2/auto-orient/thumbnail/" + 200 + "x" + size;
    }

    public static String getThumbBackgroundUrl(String imageUrl, int size) {
        return imageUrl + "?imageMogr2/auto-orient/thumbnail/" + size + "x" + size
                + "/extent/" + size + "x" + size + "/background/IzJmMzY0Zg==";
    }

    public static String getBookThumbBackgroundUrl(String imageUrl, double width, double height) {

        return imageUrl + "?imageMogr2/auto-orient/thumbnail/" + width + "x" + height
                + "/extent/" + width + "x" + height + "/background/IzJmMzY0Zg==";
    }

    /**
     * 通过外链获取图片key
     *
     * @param fullImageUrl 原始图url
     * @return thumbImageUrl
     */
    public static String getImageKeyByFullUrl(String fullImageUrl) {
        String IMAGE_DOMAIN = "https://img.rakbow.com/";
        return fullImageUrl.replace(IMAGE_DOMAIN, "");
    }

    public static String getThumb70Url(String imagesJson) {
        JSONArray images = JSON.parseArray(imagesJson);
        if (images.size() != 0) {
            for (int i = 0; i < images.size(); i++) {
                JSONObject image = images.getJSONObject(i);
                if (Objects.equals(image.getString("type"), "1")) {
                    return getThumbBackgroundUrl(image.getString("url"), 70);
                }
            }
        }
        return getThumbUrl(CommonConstant.EMPTY_IMAGE_URL, 70);
    }

}
