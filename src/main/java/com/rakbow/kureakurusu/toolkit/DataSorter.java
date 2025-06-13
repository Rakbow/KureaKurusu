package com.rakbow.kureakurusu.toolkit;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.entity.Episode;
import com.rakbow.kureakurusu.data.entity.Entry;
import com.rakbow.kureakurusu.data.entity.item.Item;
import com.rakbow.kureakurusu.data.entity.resource.Image;
import com.rakbow.kureakurusu.data.vo.EntityResourceCount;

import java.util.Comparator;

/**
 * @author Rakbow
 * @since 2022-11-06 3:39
 */
public class DataSorter {

    public static Comparator<Episode> episodeIdSorter = Comparator.comparingLong(Episode::getId);
    public static Comparator<Item> itemIdSorter = Comparator.comparingLong(Item::getId);
    public static Comparator<Entry> entryIdSorter = Comparator.comparingLong(Entry::getId);
    public static Comparator<Entry> entryDateSorter = Comparator.comparing(Entry::getDate);
    public static Comparator<Attribute<Long>> attributesLongValueSorter = Comparator.comparingLong(Attribute<Long>::getValue);
    public static Comparator<Attribute<Integer>> attributesIntValueSorter = Comparator.comparingInt(Attribute<Integer>::getValue);
    public static Comparator<Attribute<String>> attributesStringValueSorter = Comparator.comparing(Attribute<String>::getValue);
    public static Comparator<Episode> episodeSortByDiscNoAndSerial = Comparator.comparingInt(Episode::getDiscNo)
            .thenComparingInt(Episode::getSerial);
    public static Comparator<Image> imageEntityTypeEntityIdTypeSorter = Comparator.comparingInt(Image::getEntityType)
            .thenComparingLong(Image::getEntityId).thenComparingInt(Image::getType);

    public static Comparator<EntityResourceCount> entityResourceCountEntityTypeEntityIdSorter = Comparator.comparingInt(EntityResourceCount::getEntityType)
            .thenComparingLong(EntityResourceCount::getEntityId);

}

