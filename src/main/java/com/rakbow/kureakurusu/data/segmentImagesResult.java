package com.rakbow.kureakurusu.data;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rakbow
 * @since 2022-12-31 1:18
 */
public class segmentImagesResult {

    //添加了缩略图的images
    public JSONArray images;
    //封面图片
    public JSONObject cover;
    //展示图片
    public JSONArray displayImages;
    //其他图片
    public JSONArray otherImages;

    public segmentImagesResult () {
        this.images = new JSONArray();
        this.cover = new JSONObject();
        this.displayImages = new JSONArray();
        this.otherImages = new JSONArray();
    }

}
