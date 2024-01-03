package com.rakbow.kureakurusu.dao;

import com.rakbow.kureakurusu.entity.Book;
import com.rakbow.kureakurusu.entity.Entry;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Rakbow
 * @since 2023-04-29 17:38
 */
@Mapper
public interface EntryMapper {

    Entry getEntry(int id);

    List<Entry> getEntries(List<Integer> ids);

    List<Entry> getAll();

    void addEntry(Entry entry);

    void updateEntry(int id, Entry entry);

    void deleteEntry(int id);

    List<Entry> getEntryByCategory(int category);

    //根据过滤条件搜索Entry
    List<Entry> getEntriesByFilter(String name, String nameZh, String nameEn, int category,
                                   String sortField, int sortOrder, int first, int row);

    //超详细查询条数
    int getEntriesRowsByFilter(String name, String nameZh, String nameEn, int category);

}
