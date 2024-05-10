package com.rakbow.kureakurusu.toolkit;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.entity.*;
import com.rakbow.kureakurusu.data.entity.Item;

import java.util.Comparator;

/**
 * @author Rakbow
 * @since 2022-11-06 3:39
 */
public class DataSorter {

    public static ProductSortById productIdSorter = new ProductSortById();
    public static AttributesSortByIntValue attributesIntValueSorter = new AttributesSortByIntValue();
    public static AttributesSortByLongValue attributesLongValueSorter = new AttributesSortByLongValue();
    public static AttributesSortByStringValue attributesStringValueSorter = new AttributesSortByStringValue();
    public static PersonSortById personIdSorter = new PersonSortById();
    public static EpisodeSortById episodeIdSorter = new EpisodeSortById();
    public static FranchiseSortById franchiseIdSorter = new FranchiseSortById();
    public static ItemSortById itemIdSorter = new ItemSortById();

}

class ItemSortById implements Comparator<Item> {
    @Override
    public int compare(Item a, Item b) {
        return a.getId().compareTo(b.getId());
    }
}

class ProductSortById implements Comparator<Product> {
    @Override
    public int compare(Product a, Product b) {
        return Long.compare(a.getId(), b.getId());
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

