package com.rakbow.kureakurusu.toolkit;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.entity.Episode;
import com.rakbow.kureakurusu.data.entity.entry.Entry;
import com.rakbow.kureakurusu.data.entity.entry.Person;
import com.rakbow.kureakurusu.data.entity.entry.Product;
import com.rakbow.kureakurusu.data.entity.entry.Subject;
import com.rakbow.kureakurusu.data.entity.item.Item;
import com.rakbow.kureakurusu.data.entity.resource.Image;

import java.util.Comparator;

/**
 * @author Rakbow
 * @since 2022-11-06 3:39
 */
public class DataSorter {

    public static Comparator<Episode> episodeIdSorter = Comparator.comparingLong(Episode::getId);
    public static Comparator<Product> productIdSorter = Comparator.comparingLong(Product::getId);
    public static Comparator<Item> itemIdSorter = Comparator.comparingLong(Item::getId);
    public static Comparator<Entry> entryIdSorter = Comparator.comparingLong(Entry::getId);
    public static Comparator<Subject> subjectIdSorter = Comparator.comparingLong(Subject::getId);
    public static Comparator<Person> personIdSorter = Comparator.comparingLong(Person::getId);
    public static Comparator<Product> productDateSorter = Comparator.comparing(Product::getDate);
    public static Comparator<Attribute<Long>> attributesLongValueSorter = Comparator.comparingLong(Attribute<Long>::getValue);
    public static Comparator<Attribute<Integer>> attributesIntValueSorter = Comparator.comparingInt(Attribute<Integer>::getValue);
    public static Comparator<Attribute<String>> attributesStringValueSorter = Comparator.comparing(Attribute<String>::getValue);
    public static Comparator<Episode> episodeSortByDiscNoAndSerial = Comparator.comparing(Episode::getDiscNo)
            .thenComparing(Episode::getSerial);
    public static Comparator<Image> imageEntityTypeEntityIdTypeSorter = Comparator.comparingInt(Image::getEntityType)
            .thenComparingLong(Image::getEntityId).thenComparingInt(Image::getType);

}

