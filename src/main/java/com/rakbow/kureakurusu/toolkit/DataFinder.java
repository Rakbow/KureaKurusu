package com.rakbow.kureakurusu.toolkit;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.entity.*;
import com.rakbow.kureakurusu.data.entity.entry.Entry;
import com.rakbow.kureakurusu.data.entity.entry.Person;
import com.rakbow.kureakurusu.data.entity.entry.Product;
import com.rakbow.kureakurusu.data.entity.entry.Subject;
import com.rakbow.kureakurusu.data.entity.item.Item;
import com.rakbow.kureakurusu.data.entity.resource.Image;

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
    public static Item itemFinder = new Item();
    public static Product productFinder = new Product();
    public static Entry entityFinder = new Entry();
    public static Subject subjectFinder = new Subject();
    public static Image imageFinder = new Image();

    //region album

    public static Entry findEntityById(Long id, List<Entry> list) {
        entityFinder.setId(id);
        int idx = Collections.binarySearch(list, entityFinder, DataSorter.entryIdSorter);
        return idx >= 0 ? list.get(idx) : null;
    }

    public static Subject findEntryById(Long id, List<Subject> list) {
        subjectFinder.setId(id);
        int idx = Collections.binarySearch(list, subjectFinder, DataSorter.subjectIdSorter);
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

    public static Product findProductById(Long id, List<Product> list) {
        productFinder.setId(id);
        int idx = Collections.binarySearch(list, productFinder, DataSorter.productIdSorter);
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

    public static Person findPersonById(long id, List<Person> list) {
        personFinder.setId(id);
        int idx = Collections.binarySearch(list, personFinder, DataSorter.personIdSorter);
        return idx >= 0 ? list.get(idx) : null;
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

}
