package com.rakbow.kureakurusu.toolkit;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.entity.*;
import com.rakbow.kureakurusu.data.entity.common.MetaEntity;
import com.rakbow.kureakurusu.data.image.Image;

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
    public static MetaEntity entityFinder = new MetaEntity();
    public static Entry entryFinder = new Entry();
    public static Image imageFinder = new Image();

    //region album

    public static MetaEntity findEntityById(Long id, List<MetaEntity> items) {
        entityFinder.setId(id);
        int idx = Collections.binarySearch(items, entityFinder, DataSorter.entitySortById);
        if (idx >= 0) return items.get(idx);
        return null;
    }

    public static Entry findEntryById(Long id, List<Entry> items) {
        entryFinder.setId(id);
        int idx = Collections.binarySearch(items, entryFinder, DataSorter.entryIdSorter);
        if (idx >= 0) return items.get(idx);
        return null;
    }

    public static Image findImageByEntityTypeEntityIdType(int entityType, long entityId, int type, List<Image> images) {
        imageFinder.setEntityType(entityType);
        imageFinder.setEntityId(entityId);
        imageFinder.setType(type);
        int idx = Collections.binarySearch(images, imageFinder, DataSorter.imageEntityTypeEntityIdTypeSorter);
        if (idx >= 0) return images.get(idx);
        return null;
    }

    public static Item findItemById(Long id, List<Item> items) {
        itemFinder.setId(id);
        int idx = Collections.binarySearch(items, itemFinder, DataSorter.itemIdSorter);
        if (idx >= 0) return items.get(idx);
        return null;
    }

    public static Product findProductById(Long id, List<Product> products) {
        productFinder.setId(id);
        int idx = Collections.binarySearch(products, productFinder, DataSorter.productIdSorter);
        if (idx >= 0) return products.get(idx);
        return null;
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

        if (values.length == 0) return res;

        Attribute<Integer> finder = new Attribute<>();
        for (int value : values) {
            finder.setValue(value);
            int idx = Collections.binarySearch(attributes, finder, DataSorter.attributesIntValueSorter);
            if (idx >= 0) {
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

}
