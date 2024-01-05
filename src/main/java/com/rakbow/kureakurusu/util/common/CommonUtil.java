package com.rakbow.kureakurusu.util.common;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.emun.image.ImageType;
import com.rakbow.kureakurusu.util.I18nHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Rakbow
 * @since 2022-08-02 0:38
 */
public class CommonUtil {

    //删除服务器上的文件
    //dir: 文件夹路径，fileName: 文件名（不包含后缀）
    public static void deleteFile(Path dir, String fileName){
        File[] files = new File(dir.toUri()).listFiles();
        assert files != null;
        for (File file : files) {
            if (file.getName().substring(0, file.getName().lastIndexOf(".")).equals(fileName)) {
                file.delete();
            }
        }
    }

    //计算时间总和（返回字符串形式）
    public static String countTotalTime(List<String> times){

        int totalHour = 0;
        int totalMin = 0;
        int totalSecond = 0;
        String hour = "";
        String min = "";
        String sec = "";

        for(String time : times){
            //如果有两个":"表示时长在一小时以上
            if(time.indexOf(":") != time.lastIndexOf(":")){
                totalHour += Integer.parseInt(time.substring(0,time.indexOf(":")));
                totalMin += Integer.parseInt(time.substring(time.indexOf(":")+1, time.lastIndexOf(":")));
                totalSecond += Integer.parseInt(time.substring(time.lastIndexOf(":")+1));
            }else {
                totalMin += Integer.parseInt(time.substring(0, time.indexOf(":")));
                totalSecond += Integer.parseInt(time.substring(time.indexOf(":")+1));
            }

            //处理进位
            if(totalSecond >= 60){
                totalSecond = totalSecond%60;
                totalMin++;
            }
            if(totalMin >= 60){
                totalMin = totalMin%60;
                totalHour++;
            }
        }
        hour = (totalHour < 10)? "0" + totalHour:Integer.toString(totalHour);
        min = (totalMin < 10)? "0" + totalMin:Integer.toString(totalMin);
        sec = (totalSecond < 10)? "0" + totalSecond:Integer.toString(totalSecond);
        if(hour.equals("00")){
            return min + ":" + sec;
        }else {
            return hour + ":" + min + ":" + sec;
        }
    }

    //生成随机字符串
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    //MD5加密
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    //获取JSON字符串
    public static String getJSONString(int code, String msg, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if (map != null) {
            for (String key : map.keySet()) {
                json.put(key, map.get(key));
            }
        }
        return json.toJSONString();
    }

    //递归获取jsonObject的所有value
    public static String getAllContentFromJson(Object object) {
        StringBuilder mStringBuffer = new StringBuilder();
        if(object instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) object;
            for (Map.Entry<String, Object> entry: jsonObject.entrySet()) {
                Object o = entry.getValue();
                if(o instanceof Integer){
                    mStringBuffer.append(" ").append(entry.getValue());
                }else if(o instanceof Double){
                    mStringBuffer.append(" ").append(entry.getValue());
                }else if(o instanceof Float){
                    mStringBuffer.append(" ").append(entry.getValue());
                }else if(o instanceof Byte){
                    mStringBuffer.append(" ").append(entry.getValue());
                }else if(o instanceof Long){
                    mStringBuffer.append(" ").append(entry.getValue());
                }else if(o instanceof String) {
                    try{
                        object= JSONObject.parseObject((String)o);
                        getAllContentFromJson(object);
                    }catch (Exception e){
                        mStringBuffer.append(" ").append(entry.getValue());
                    }
                }
                else {
                    getAllContentFromJson(o);
                }
            }
        }
        if(object instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) object;
            for (Object o : jsonArray) {
                getAllContentFromJson(o);
            }
        }
        return mStringBuffer.toString();
    }

    /**
     * 使用 Stream 去重list
     * @param list
     */
    public static <T> List<T> removeDuplicateList(List<T> list) {
        return list.stream().distinct().collect(Collectors.toList());
    }

    /**
     * int字符串转为int数组并排序
     * @author rakbow
     * @param arrStr
     * @return int[]
     * */
    public static String[] str2SortedArray(String arrStr) {
        String[] strArr = arrStr.substring(1,arrStr.length()-1).split(",");
        // int[] intArr = Arrays.stream(strArr).mapToInt(Integer::parseInt).toArray();
        Arrays.sort(strArr);
        return strArr;
    }

    /**
     * ids字符串转list
     *
     * @author rakbow
     * @param idsJson ids字符串
     * @return List<Integer>
     * */
    public static List<Integer> ids2List (String idsJson) {
        return new ArrayList<>(JSONObject.parseObject(idsJson).getList("ids", Integer.class));
    }

    public static List<String> getJsonArrayKeys(JSONArray jsonArray, String key) {

        if(jsonArray == null || jsonArray.isEmpty()) {
            return null;
        }
        List<String> keys = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            if(!StringUtils.isBlank(json.getString(key))) {
                keys.add(json.getString(key));
            }
        }
        return keys;
    }

    //region ------暂时废弃------

    /**
     * 压缩图片
     *
     * @author rakbow
     * @param oldFilePath 原图片路径
     * @param width 压缩宽
     * @param height 压缩高
     * @param outFile 压缩图片后,图片名称路径
     * @param percentage 是否等比压缩 若true宽高比率将将自动调整
     */
    @Deprecated
    public static String imageCompress(String oldFilePath, int width, int height, String outFile, boolean percentage) {
        if (oldFilePath != null && width > 0 && height > 0) {
            Image srcFile=null;
            try {
                File file = new File(oldFilePath);
                // 文件不存在
                if (!file.exists()) {
                    return null;
                }
                /*读取图片信息*/
                srcFile = ImageIO.read(file);
                int new_w = width;
                int new_h = height;
                if (percentage) {
                    // 为等比缩放计算输出的图片宽度及高度
                    double rate1 = ((double) srcFile.getWidth(null)) / (double) width + 0.1;
                    double rate2 = ((double) srcFile.getHeight(null)) / (double) height + 0.1;
                    double rate = Math.max(rate1, rate2);
                    new_w = (int) (((double) srcFile.getWidth(null)) / rate);
                    new_h = (int) (((double) srcFile.getHeight(null)) / rate);
                }
                /* 宽高设定*/
                BufferedImage tag = new BufferedImage(new_w, new_h, BufferedImage.TYPE_INT_RGB);
                tag.getGraphics().drawImage(srcFile, 0, 0, new_w, new_h, null);

                /*压缩之后临时存放位置*/
                FileOutputStream out = new FileOutputStream(outFile);

                ImageIO.write(tag, "JPG", out);
                out.close();

            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                assert srcFile != null;
                srcFile.flush();
            }
            return outFile;
        } else {
            return null;
        }
    }

    /**
     * 根据图片url获取图片文件全名
     *
     * @author rakbow
     * @param url 图片url
     * @return fileName
     * */
    public static String getImageFileNameByUrl (String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    /**
     * 对更新图片信息合法性进行检测，图片英文名和图片类型
     *
     * @param
     * @return
     * @author rakbow
     */
    @Deprecated
    public static String DeprecatedCheckUpdateImages(JSONArray images) {
        //封面类型的图片个数
        int coverCount = 0;
        for (int i = 0; i < images.size(); i++) {
            if (images.getJSONObject(i).getIntValue("type") == ImageType.MAIN.getValue()) {
                coverCount++;
            }
        }
        if (coverCount > 1) {
            return I18nHelper.getMessage("image.error.only_one_cover");
        }

        //检测是否存在重复英文名
        List<String> originImageUrlNameEns = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            String imageUrl = images.getJSONObject(i).getString("url");
            originImageUrlNameEns.add(imageUrl.substring(
                    imageUrl.lastIndexOf("/") + 1, imageUrl.lastIndexOf(".")));
        }
        if (originImageUrlNameEns.stream().distinct().count() != images.size()) {
            return I18nHelper.getMessage("image.error.name_en.not_repeat");
        }

        return "";
    }

    /**
     * 对新增图片信息合法性进行检测，图片英文名和图片类型
     *
     * @param imageInfos,images 新增图片信息，专辑原图片集合
     * @return boolean
     * @author rakbow
     */
    @Deprecated
    public static String DeprecatedCheckAddImages(JSONArray imageInfos, JSONArray images) {

        List<String> imageUrlNameEns = new ArrayList<>();
        int coverCount = 0;

        if (images.size() != 0) {

            for (int i = 0; i < images.size(); i++) {
                String imageUrl = images.getJSONObject(i).getString("url");
                imageUrlNameEns.add(imageUrl.substring(
                        imageUrl.lastIndexOf("/") + 1, imageUrl.lastIndexOf(".")));
            }
            for (int i = 0; i < imageInfos.size(); i++) {
                String nameEn = imageInfos.getJSONObject(i).getString("nameEn");
                imageUrlNameEns.add(nameEn.replace(" ", ""));
            }

            for (int i = 0; i < images.size(); i++) {
                if (images.getJSONObject(i).getIntValue("type") == ImageType.MAIN.getValue()) {
                    coverCount++;
                }
            }
            for (int i = 0; i < imageInfos.size(); i++) {
                if (imageInfos.getJSONObject(i).getIntValue("type") == ImageType.MAIN.getValue()) {
                    coverCount++;
                }
            }
        } else {

            for (int i = 0; i < imageInfos.size(); i++) {
                String nameEn = imageInfos.getJSONObject(i).getString("nameEn");
                imageUrlNameEns.add(nameEn.replace(" ", ""));
            }

            for (int i = 0; i < imageInfos.size(); i++) {
                if (imageInfos.getJSONObject(i).getIntValue("type") == ImageType.MAIN.getValue()) {
                    coverCount++;
                }
            }

        }

        //检测是否存在重复英文名
        int originNameUrlCount = imageUrlNameEns.size();
        if (imageUrlNameEns.stream().distinct().count() != originNameUrlCount) {
            return I18nHelper.getMessage("image.error.name_en.not_repeat");
        }

        //检测图片类型为封面的个数是否大于1
        if (coverCount > 1) {
            return I18nHelper.getMessage("image.error.only_one_cover");
        }

        return "";
    }

    /**
     * 压缩图片并返回缩略图的url
     *
     * @author rakbow
     * @param entity,id,fileName 实体类型,id和图片文件全名
     * @return 缩略图url
     * */
    @Deprecated
    public static String getCompressImageUrl(String imgPath, String entity, int entityId, String fileName) {
        Path imagePath = Paths.get(imgPath + "/" + entity + "/" + entityId);
        String oldFilePath = (imagePath + "\\" + fileName);
        String outFilePath = (imgPath + "/compress/" + entity + "/" + entityId + "/" + fileName);

        File file = new File(outFilePath);
        //判断是否存在
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        } else {
            file.getParentFile().delete();
            file.getParentFile().mkdir();
        }

        CommonUtil.imageCompress(oldFilePath, 200, 200, outFilePath, true);
        return "/db/" + entity + "/" + entityId + "/compress/" + fileName;
    }

    //打印请求消息
    public static void printRequestHeaders(HttpServletRequest req) {
        Enumeration<String> names = req.getHeaderNames();
        if(names == null) {
            return;
        }
        while(names.hasMoreElements()) {
            String name = (String) names.nextElement();
            Enumeration<String> values = req.getHeaders(name);
            if(values != null) {
                while(values.hasMoreElements()) {
                    String value = (String) values.nextElement();
                    System.out.println(name + " : " + value );
                }
            }
        }
    }

    //打印响应消息
    public static void printResponseHeaders(HttpServletResponse res) {
        Collection<String> names = res.getHeaderNames();
        if(names == null) {
            return;
        }
        for (String name : names) {
            Collection<String> values = res.getHeaders(name);
            if (values != null) {
                for (String value : values) {
                    System.out.println(name + " : " + value);
                }
            }
        }
    }

    //endregion

    public static String getIdsStr(List<Integer> ids) {
        return "{\"ids\":[" + ids.stream()
                .map(Object::toString)
                .collect(Collectors.joining(",")) + "]}";
    }

    public static String getListStr(List<String> list) {
        return list.stream()
                .map(s -> "\"" + s + "\"")
                .collect(Collectors.joining(", ", "[", "]"));
    }

    /*
    * 将字符串首字母转为小写
    * */
    public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return Character.toLowerCase(input.charAt(0)) + input.substring(1);
    }

}
