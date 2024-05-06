package com.rakbow.kureakurusu.util.common;

import com.rakbow.kureakurusu.data.dto.AlbumListQueryDTO;
import com.rakbow.kureakurusu.data.dto.BookListQueryDTO;
import com.rakbow.kureakurusu.data.dto.ItemListQueryDTO;
import com.rakbow.kureakurusu.data.dto.ListQueryDTO;
import com.rakbow.kureakurusu.data.emun.ItemType;
import com.rakbow.kureakurusu.data.entity.*;
import com.rakbow.kureakurusu.data.entity.common.SuperItem;
import com.rakbow.kureakurusu.data.vo.ItemVO;
import com.rakbow.kureakurusu.data.vo.album.AlbumVO;
import com.rakbow.kureakurusu.data.vo.test.AlbumListVO;
import com.rakbow.kureakurusu.data.vo.test.BookListVO;
import com.rakbow.kureakurusu.data.vo.test.ItemListVO;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

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

}