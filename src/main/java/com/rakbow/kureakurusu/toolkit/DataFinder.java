package com.rakbow.kureakurusu.toolkit;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.entity.Entity;
import com.rakbow.kureakurusu.data.entity.Entry;
import com.rakbow.kureakurusu.data.entity.Episode;

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
    public static Entry entryFinder = new Entry();
    public static Entity entityFinder = new Entity();

    //region album

    public static Entity findEntityById(Long id, List<? extends Entity> list) {
        entityFinder.setId(id);
        int idx = Collections.binarySearch(list, entityFinder, DataSorter.entityIdSorter);
        return idx >= 0 ? list.get(idx) : null;
    }

    public static Entry findEntryById(Long id, List<Entry> list) {
        entryFinder.setId(id);
        int idx = Collections.binarySearch(list, entryFinder, DataSorter.entryIdSorter);
        return idx >= 0 ? list.get(idx) : null;
    }

    //endregion

    //region product

    /**
     * 根据Value从指定attributes列表中查找
     *
     * @param value,list 查找id和json列表
     * @return json
     * @author rakbow
     */
    public static Attribute<Integer> findAttributeByValue(int value, List<Attribute<Integer>> list) {
        Attribute<Integer> finder = new Attribute<>();
        finder.setValue(value);
        int idx = Collections.binarySearch(list, finder, DataSorter.attributesIntValueSorter);
        return idx >= 0 ? list.get(idx) : null;
    }

    public static Attribute<Long> findAttributeByValue(long value, List<Attribute<Long>> list) {
        Attribute<Long> finder = new Attribute<>();
        finder.setValue(value);
        int idx = Collections.binarySearch(list, finder, DataSorter.attributesLongValueSorter);
        return idx >= 0 ? list.get(idx) : null;
    }

    //endregion

    public static Episode findEpisodeBySerial(int Serial, List<Episode> list) {
        episodeFinder.setSerial(Serial);
        int idx = Collections.binarySearch(list, episodeFinder, DataSorter.episodeSerialSorter);
        return idx >= 0 ? list.get(idx) : null;
    }

}
