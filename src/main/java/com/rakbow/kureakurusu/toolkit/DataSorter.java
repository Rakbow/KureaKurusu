package com.rakbow.kureakurusu.toolkit;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.entity.*;
import com.rakbow.kureakurusu.data.entity.entry.Person;
import com.rakbow.kureakurusu.data.entity.entry.Product;
import com.rakbow.kureakurusu.data.entity.entry.Subject;
import com.rakbow.kureakurusu.data.entity.item.Item;
import com.rakbow.kureakurusu.data.entity.entry.Entry;
import com.rakbow.kureakurusu.data.entity.resource.Image;

import java.util.Comparator;

/**
 * @author Rakbow
 * @since 2022-11-06 3:39
 */
public class DataSorter {

    public static ProductSortById productIdSorter = new ProductSortById();
    public static ProductSortByDate productDateSorter = new ProductSortByDate();
    public static AttributesSortByIntValue attributesIntValueSorter = new AttributesSortByIntValue();
    public static AttributesSortByLongValue attributesLongValueSorter = new AttributesSortByLongValue();
    public static AttributesSortByStringValue attributesStringValueSorter = new AttributesSortByStringValue();
    public static PersonSortById personIdSorter = new PersonSortById();
    public static EpisodeSortById episodeIdSorter = new EpisodeSortById();
    public static ItemSortById itemIdSorter = new ItemSortById();
    public static EntitySortById entitySortById = new EntitySortById();
    public static EntrySortById entryIdSorter = new EntrySortById();
    public static ImageSortByEntityTypeEntityIdType imageEntityTypeEntityIdTypeSorter = new ImageSortByEntityTypeEntityIdType();

}

class ImageSortByEntityTypeEntityIdType implements Comparator<Image> {
    @Override
    public int compare(Image a, Image b) {
        return Comparator.comparing(Image::getEntityType)
                .thenComparing(Image::getEntityId)
                .thenComparing(Image::getType)
                .compare(a, b);
    }
}

class EntrySortById implements Comparator<Subject> {
    @Override
    public int compare(Subject a, Subject b) {
        return a.getId().compareTo(b.getId());
    }
}

class EntitySortById implements Comparator<Entry> {
    @Override
    public int compare(Entry a, Entry b) {
        return a.getId().compareTo(b.getId());
    }
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

class ProductSortByDate implements Comparator<Product> {
    @Override
    public int compare(Product a, Product b) {
        return a.getDate().compareTo(b.getDate());
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

