package com.rakbow.kureakurusu.util.common;

import com.alibaba.fastjson2.JSONObject;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.entity.*;

import java.util.Comparator;

/**
 * @author Rakbow
 * @since 2022-11-06 3:39
 */
public class DataSorter {

    public static AlbumSortById albumIdSorter = new AlbumSortById();
    public static MusicSortById musicIdSorter = new MusicSortById();
    public static MusicSortByTrackSerial musicTrackSerialSorter = new MusicSortByTrackSerial();
    public static JsonSortById jsonIdSorter = new JsonSortById();
    public static JsonSetSortByValue jsonSetValueSorter = new JsonSetSortByValue();
    public static AttributesSortByIntValue attributesIntValueSorter = new AttributesSortByIntValue();
    public static AttributesSortByLongValue attributesLongValueSorter = new AttributesSortByLongValue();
    public static AttributesSortByStringValue attributesStringValueSorter = new AttributesSortByStringValue();
    public static PersonSortById personIdSorter = new PersonSortById();
    public static EpisodeSortById episodeIdSorter = new EpisodeSortById();
    public static FranchiseSortById franchiseIdSorter = new FranchiseSortById();

}

class AlbumSortById implements Comparator<Album> {
    @Override
    public int compare(Album a, Album b) {
        return Long.compare(a.getId(), b.getId());
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

class AttributesSortByIntValue implements Comparator<Attribute<Integer>> {
    @Override
    public int compare(Attribute<Integer> a, Attribute<Integer> b) {
        return Integer.compare(a.getValue(), b.getValue());
    }
}

class AttributesSortByLongValue implements Comparator<Attribute<Long>> {
    @Override
    public int compare(Attribute<Long> a, Attribute<Long> b) {
        return Long.compare(a.getValue(), b.getValue());
    }
}

class AttributesSortByStringValue implements Comparator<Attribute<String>> {
    @Override
    public int compare(Attribute<String> a, Attribute<String> b) {
        return a.getValue().compareTo(b.getValue());
    }
}

class PersonSortById implements Comparator<Person> {
    @Override
    public int compare(Person a, Person b) {
        return Long.compare(a.getId(), b.getId());
    }
}

class EpisodeSortById implements Comparator<Episode> {
    @Override
    public int compare(Episode a, Episode b) {
        return Long.compare(a.getId(), b.getId());
    }
}

class FranchiseSortById implements Comparator<Franchise> {
    @Override
    public int compare(Franchise a, Franchise b) {
        return Long.compare(a.getId(), b.getId());
    }
}

