package com.rakbow.kureakurusu.util.common;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.entity.Album;
import com.rakbow.kureakurusu.data.entity.Episode;
import com.rakbow.kureakurusu.data.entity.Franchise;
import com.rakbow.kureakurusu.data.entity.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 数据查找
 *
 * @author Rakbow
 * @since 2022-11-06 5:54
 */
public class DataFinder {

    public static Episode episodeFinder = new Episode();
    public static Person personFinder = new Person();
    public static Franchise franchiseFinder = new Franchise();

    //region album

    public static Album findAlbumById(Long id, List<Album> albums) {
        Album finder = new Album();
        finder.setId(id);
        int idx = Collections.binarySearch(albums, finder, DataSorter.albumIdSorter);
        if (idx >= 0) {
            return albums.get(idx);
        }else {
            return null;
        }
    }

    //endregion

    //region product

    /**
     * 根据Value从指定attributes列表中查找
     *
     * @param value,attributes 查找id和json列表
     * @return json
     * @author rakbow
     */
    public static Attribute<Integer> findAttributeByValue(int value, List<Attribute<Integer>> attributes) {
        Attribute<Integer> finder = new Attribute<>();
        finder.setValue(value);
        int idx = Collections.binarySearch(attributes, finder, DataSorter.attributesIntValueSorter);
        return idx >= 0 ? attributes.get(idx) : null;
    }

    public static Attribute<Long> findAttributeByValue(long value, List<Attribute<Long>> attributes) {
        Attribute<Long> finder = new Attribute<>();
        finder.setValue(value);
        int idx = Collections.binarySearch(attributes, finder, DataSorter.attributesLongValueSorter);
        return idx >= 0 ? attributes.get(idx) : null;
    }

    public static Attribute<String> findAttributeByValue(String value, List<Attribute<String>> attributes) {
        Attribute<String> finder = new Attribute<>();
        finder.setValue(value);
        int idx = Collections.binarySearch(attributes, finder, DataSorter.attributesStringValueSorter);
        return idx >= 0 ? attributes.get(idx) : null;
    }

    public static List<Attribute<Integer>> findAttributesByValues(int[] values, List<Attribute<Integer>> attributes) {

        List<Attribute<Integer>> res = new ArrayList<>();

        if(values.length == 0) return res;

        Attribute<Integer> finder = new Attribute<>();
        for(int value : values) {
            finder.setValue(value);
            int idx = Collections.binarySearch(attributes, finder, DataSorter.attributesIntValueSorter);
            if(idx >= 0) {
                res.add(attributes.get(idx));
            }
        }
        return res;
    }

    public static Person findPersonById(long id, List<Person> persons) {
        personFinder.setId(id);
        int idx = Collections.binarySearch(persons, personFinder, DataSorter.personIdSorter);
        return idx >= 0 ? persons.get(idx) : null;
    }

    //endregion

    public static Episode findEpisodeById(long id, List<Episode> episodes) {
        episodeFinder.setId(id);
        int idx = Collections.binarySearch(episodes, episodeFinder, DataSorter.episodeIdSorter);
        return idx >= 0 ? episodes.get(idx) : null;
    }

    public static Franchise findFranchiseById(long id, List<Franchise> franchises) {
        franchiseFinder.setId(id);
        int idx = Collections.binarySearch(franchises, franchiseFinder, DataSorter.franchiseIdSorter);
        return idx >= 0 ? franchises.get(idx) : null;
    }

}
