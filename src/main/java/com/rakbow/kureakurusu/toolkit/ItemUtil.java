package com.rakbow.kureakurusu.toolkit;

import com.rakbow.kureakurusu.data.dto.AlbumListQueryDTO;
import com.rakbow.kureakurusu.data.dto.BookListQueryDTO;
import com.rakbow.kureakurusu.data.dto.ItemListQueryDTO;
import com.rakbow.kureakurusu.data.dto.ListQueryDTO;
import com.rakbow.kureakurusu.data.emun.ItemType;
import com.rakbow.kureakurusu.data.entity.*;
import com.rakbow.kureakurusu.data.entity.common.SuperItem;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.data.vo.item.ItemVO;
import com.rakbow.kureakurusu.data.vo.item.AlbumVO;
import com.rakbow.kureakurusu.data.vo.item.BookVO;
import com.rakbow.kureakurusu.data.vo.item.AlbumListVO;
import com.rakbow.kureakurusu.data.vo.item.BookListVO;
import com.rakbow.kureakurusu.data.vo.item.ItemListVO;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Rakbow
 * @since 2024/4/26 17:04
 */
public class ItemUtil {

    private final static Map<Integer, Class<? extends SubItem>> subItemMap = new HashMap<>() {{
        put(ItemType.ALBUM.getValue(), ItemAlbum.class);
        put(ItemType.BOOK.getValue(), ItemBook.class);
    }};
    private final static Map<Integer, Class<? extends SuperItem>> SuperItemMap = new HashMap<>() {{
        put(ItemType.ALBUM.getValue(), Album.class);
        put(ItemType.BOOK.getValue(), Book.class);
    }};

    private final static Map<Integer, Class<? extends ItemListVO>> itemListVOMap = new HashMap<>() {{
        put(ItemType.ALBUM.getValue(), AlbumListVO.class);
        put(ItemType.BOOK.getValue(), BookListVO.class);
    }};

    private final static Map<Integer, Class<? extends ItemVO>> itemDetailVOMap = new HashMap<>() {{
        put(ItemType.ALBUM.getValue(), AlbumVO.class);
        put(ItemType.BOOK.getValue(), BookVO.class);
    }};

    private final static Map<Integer, Class<? extends ItemListQueryDTO>> itemListQryMap = new HashMap<>() {{
        put(ItemType.ALBUM.getValue(), AlbumListQueryDTO.class);
        put(ItemType.BOOK.getValue(), BookListQueryDTO.class);
    }};

    public static Class<? extends SubItem> getSubItem(int type) {
        return subItemMap.get(type);
    }

    public static Class<? extends SuperItem> getSuperItem(int type) {
        return SuperItemMap.get(type);
    }

    public static Class<? extends ItemListVO> getItemListVO(int type) {
        return itemListVOMap.get(type);
    }

    public static Class<? extends ItemVO> getItemDetailVO(int type) {
        return itemDetailVOMap.get(type);
    }

    @SneakyThrows
    public static ItemListQueryDTO getItemListQueryDTO(ListQueryDTO qry) {
        Class<? extends ItemListQueryDTO> queryClass = itemListQryMap.get(qry.getItemType());
        Constructor<? extends ItemListQueryDTO> constructor = queryClass.getConstructor(ListQueryDTO.class);
        return constructor.newInstance(qry);
    }

    public static Map<String, Object> getOptions(int type) {
        Map<String, Object> res = new HashMap<>();
        if(type == ItemType.ALBUM.getValue()) {
            res.put("albumFormatSet", Objects.requireNonNull(MetaData.getOptions()).albumFormatSet);
            res.put("mediaFormatSet", Objects.requireNonNull(MetaData.getOptions()).mediaFormatSet);
        }else if(type == ItemType.BOOK.getValue()) {
            res.put("languageSet", Objects.requireNonNull(MetaData.getOptions()).languageSet);
            res.put("bookTypeSet", Objects.requireNonNull(MetaData.getOptions()).bookTypeSet);
        }
        res.put("releaseTypeSet", Objects.requireNonNull(MetaData.getOptions()).releaseTypeSet);
        return res;
    }

}