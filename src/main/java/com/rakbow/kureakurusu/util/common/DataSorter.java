package com.rakbow.kureakurusu.util.common;

import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.vo.album.AlbumVOAlpha;
import com.rakbow.kureakurusu.data.vo.book.BookVOBeta;
import com.rakbow.kureakurusu.data.vo.disc.DiscVOAlpha;
import com.rakbow.kureakurusu.data.vo.game.GameVOAlpha;
import com.rakbow.kureakurusu.data.vo.merch.MerchVOAlpha;
import com.rakbow.kureakurusu.entity.Album;
import com.rakbow.kureakurusu.entity.Music;

import java.util.Comparator;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2022-11-06 3:39
 * @Description:
 */
public class DataSorter {

    public static AlbumSortById albumSortById = new AlbumSortById();
    public static MusicSortById musicSortById = new MusicSortById();
    public static MusicSortByTrackSerial musicSortByTrackSerial = new MusicSortByTrackSerial();
    public static JsonSortById jsonSortById = new JsonSortById();
    public static JsonSetSortByValue jsonSetSortByValue = new JsonSetSortByValue();
    public static AttributesSortByValue attributesSortByValue = new AttributesSortByValue();

}

class AlbumSortById implements Comparator<Album> {

    @Override
    public int compare(Album a, Album b) {
        return Integer.compare(a.getId(), b.getId());
    }
}

class MusicSortById implements Comparator<Music> {

    @Override
    public int compare(Music a, Music b) {
        return Integer.compare(a.getId(), b.getId());
    }
}

class MusicSortByTrackSerial implements Comparator<Music> {

    @Override
    public int compare(Music a, Music b) {
        return Integer.compare(Integer.parseInt(a.getTrackSerial()), Integer.parseInt(b.getTrackSerial()));
    }
}

class JsonSortById implements Comparator<JSONObject> {

    @Override
    public int compare(JSONObject a, JSONObject b) {
        return Integer.compare(a.getInteger("id"), b.getInteger("id"));
    }
}

class JsonSetSortByValue implements Comparator<JSONObject> {

    @Override
    public int compare(JSONObject a, JSONObject b) {
        return Integer.compare(a.getInteger("value"), b.getInteger("value"));
    }
}

class AttributesSortByValue implements Comparator<Attribute<Integer>> {

    @Override
    public int compare(Attribute<Integer> a, Attribute<Integer> b) {
        return Integer.compare(a.getValue(), b.getValue());
    }
}

