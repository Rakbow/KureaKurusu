package com.rakbow.kureakurusu.toolkit;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.entity.Entity;
import com.rakbow.kureakurusu.data.entity.Entry;
import com.rakbow.kureakurusu.data.entity.Episode;
import com.rakbow.kureakurusu.data.entity.item.Item;

import java.util.Comparator;

/**
 * @author Rakbow
 * @since 2022-11-06 3:39
 */
public class DataSorter {

    public static Comparator<Episode> episodeSerialSorter = Comparator.comparingLong(Episode::getSerial);
    public static Comparator<Item> itemIdSorter = Comparator.comparingLong(Item::getId);
    public static Comparator<Entry> entryIdSorter = Comparator.comparingLong(Entry::getId);
    public static Comparator<Entity> entityIdSorter = Comparator.comparingLong(Entity::getId);
    public static Comparator<Attribute<Long>> attributesLongValueSorter = Comparator.comparingLong(Attribute<Long>::getValue);
    public static Comparator<Attribute<Integer>> attributesIntValueSorter = Comparator.comparingInt(Attribute<Integer>::getValue);
    public static Comparator<Attribute<String>> attributesStringValueSorter = Comparator.comparing(Attribute<String>::getValue);
}

