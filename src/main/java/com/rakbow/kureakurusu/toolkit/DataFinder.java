package com.rakbow.kureakurusu.toolkit;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.entity.*;
import com.rakbow.kureakurusu.data.entity.Entry;
import com.rakbow.kureakurusu.data.entity.item.Item;
import com.rakbow.kureakurusu.data.entity.resource.Image;
import com.rakbow.kureakurusu.data.vo.EntityRelatedCount;

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
    public static Item itemFinder = new Item();
    public static Entry entryFinder = new Entry();
    public static Entity entityFinder = new Entity();
    public static Image imageFinder = new Image();

    public static EntityRelatedCount resourceCountFinder = new EntityRelatedCount();

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

    public static Image findImageByEntityTypeEntityIdType(int entityType, long entityId, int type, List<Image> list) {
        imageFinder.setEntityType(entityType);
        imageFinder.setEntityId(entityId);
        imageFinder.setType(type);
        int idx = Collections.binarySearch(list, imageFinder, DataSorter.imageEntityTypeEntityIdTypeSorter);
        return idx >= 0 ? list.get(idx) : null;
    }

    public static Item findItemById(Long id, List<Item> list) {
        itemFinder.setId(id);
        int idx = Collections.binarySearch(list, itemFinder, DataSorter.itemIdSorter);
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

    public static Attribute<String> findAttributeByValue(String value, List<Attribute<String>> list) {
        Attribute<String> finder = new Attribute<>();
        finder.setValue(value);
        int idx = Collections.binarySearch(list, finder, DataSorter.attributesStringValueSorter);
        return idx >= 0 ? list.get(idx) : null;
    }

    public static List<Attribute<Integer>> findAttributesByValues(int[] values, List<Attribute<Integer>> list) {

        List<Attribute<Integer>> res = new ArrayList<>();

        if (values.length == 0) return res;

        Attribute<Integer> finder = new Attribute<>();
        for (int value : values) {
            finder.setValue(value);
            int idx = Collections.binarySearch(list, finder, DataSorter.attributesIntValueSorter);
            if (idx >= 0) {
                res.add(list.get(idx));
            }
        }
        return res;
    }

    //endregion

    public static Episode findEpisodeById(long id, List<Episode> list) {
        episodeFinder.setId(id);
        int idx = Collections.binarySearch(list, episodeFinder, DataSorter.episodeIdSorter);
        return idx >= 0 ? list.get(idx) : null;
    }

    public static Episode findEpisodeByDiscNoAndSerial(int discNo, int serial, List<Episode> list) {
        episodeFinder.setDiscNo(discNo);
        episodeFinder.setSerial(serial);
        int idx = Collections.binarySearch(list, episodeFinder, DataSorter.episodeSortByDiscNoAndSerial);
        return idx >= 0 ? list.get(idx) : null;
    }

    public static EntityRelatedCount findResourceCountByTypeAndId(int type, long id, List<EntityRelatedCount> list) {
        resourceCountFinder.setEntityType(type);
        resourceCountFinder.setEntityId(id);
        int idx = Collections.binarySearch(list, resourceCountFinder, DataSorter.entityResourceCountEntityTypeEntityIdSorter);
        return idx >= 0 ? list.get(idx) : null;
    }

}
