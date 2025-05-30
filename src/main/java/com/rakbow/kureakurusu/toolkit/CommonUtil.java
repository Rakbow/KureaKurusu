package com.rakbow.kureakurusu.toolkit;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Rakbow
 * @since 2022-08-02 0:38
 */
public class CommonUtil {

    public static String getSubName(String nameEn, String nameZh) {
        return (nameEn != null && !nameEn.isBlank() && nameZh != null && !nameZh.isBlank())
                ? STR."\{nameEn} / \{nameZh}"
                : (nameEn != null && !nameEn.isBlank() ? nameEn : (nameZh != null && !nameZh.isBlank() ? nameZh : ""));
    }

    public static String camelToUnderline(String s) {
        return com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(s);
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

    public static String generateUUID(int length) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return length == 0 ? uuid : uuid.substring(0, length);
    }

    //MD5加密
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    /**
     * 使用 Stream 去重list
     */
    public static <T> List<T> removeDuplicateList(List<T> list) {
        return list.stream().distinct().collect(Collectors.toList());
    }

    /**
     * int字符串转为int数组并排序
     * @author rakbow
     * @return int[]
     * */
    public static String[] str2SortedArray(String arrStr) {
        String[] strArr = arrStr.substring(1,arrStr.length()-1).split(",");
        // int[] intArr = Arrays.stream(strArr).mapToInt(Integer::parseInt).toArray();
        Arrays.sort(strArr);
        return strArr;
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
                .map(s -> "\"" + s.replace("\n", "") + "\"")
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
