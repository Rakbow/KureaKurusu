package com.rakbow.kureakurusu.toolkit;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.entity.*;

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
    public static Album albumFinder = new Album();
    public static Item itemFinder = new Item();
    public static Product productFinder = new Product();

    //region album

    public static Item findItemById(Long id, List<Item> items) {
        albumFinder.setId(id);
        int idx = Collections.binarySearch(items, itemFinder, DataSorter.itemIdSorter);
        if (idx >= 0) return items.get(idx);
        return null;
    }

    public static Album findAlbumById(Long id, List<Album> albums) {
        albumFinder.setId(id);
        int idx = Collections.binarySearch(albums, albumFinder, DataSorter.albumIdSorter);
        if (idx >= 0) return albums.get(idx);
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
