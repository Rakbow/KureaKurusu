package com.rakbow.kureakurusu.toolkit;

import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.dto.AlbumListQueryDTO;
import com.rakbow.kureakurusu.data.dto.BookListQueryDTO;
import com.rakbow.kureakurusu.data.dto.GoodsListQueryDTO;
import com.rakbow.kureakurusu.data.dto.ItemListQueryDTO;
import com.rakbow.kureakurusu.data.emun.ItemType;
import com.rakbow.kureakurusu.data.emun.RelatedGroup;
import com.rakbow.kureakurusu.data.entity.item.SuperItem;
import com.rakbow.kureakurusu.data.entity.item.*;
import com.rakbow.kureakurusu.data.vo.item.*;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * @author Rakbow
 * @since 2024/4/26 17:04
 */
public class ItemUtil {

    public final static List<Integer> ItemExcRelatedGroups = Arrays.asList(
            RelatedGroup.RELATED_SUBJECT.getValue(),
            RelatedGroup.EVENT.getValue(),
            RelatedGroup.MATERIAL.getValue()
    );

    private final static Map<Integer, Class<? extends SubItem>> subItemMap = new HashMap<>() {{
        put(ItemType.ALBUM.getValue(), ItemAlbum.class);
        put(ItemType.BOOK.getValue(), ItemBook.class);
        put(ItemType.GOODS.getValue(), ItemGoods.class);
        put(ItemType.FIGURE.getValue(), ItemFigure.class);
    }};
    private final static Map<Integer, Class<? extends SuperItem>> SuperItemMap = new HashMap<>() {{
        put(ItemType.ALBUM.getValue(), Album.class);
        put(ItemType.BOOK.getValue(), Book.class);
        put(ItemType.GOODS.getValue(), Goods.class);
        put(ItemType.FIGURE.getValue(), Figure.class);
    }};

    private final static Map<Integer, Class<? extends ItemListVO>> itemListVOMap = new HashMap<>() {{
        put(ItemType.ALBUM.getValue(), AlbumListVO.class);
        put(ItemType.BOOK.getValue(), BookListVO.class);
        put(ItemType.GOODS.getValue(), GoodsListVO.class);
        put(ItemType.FIGURE.getValue(), FigureListVO.class);
    }};

    private final static Map<Integer, Class<? extends ItemVO>> itemDetailVOMap = new HashMap<>() {{
        put(ItemType.ALBUM.getValue(), AlbumVO.class);
        put(ItemType.BOOK.getValue(), BookVO.class);
        put(ItemType.GOODS.getValue(), GoodsVO.class);
        put(ItemType.FIGURE.getValue(), FigureVO.class);
    }};

    private final static Map<Integer, Class<? extends ItemListQueryDTO>> itemListQryMap = new HashMap<>() {{
        put(ItemType.ALBUM.getValue(), AlbumListQueryDTO.class);
        put(ItemType.BOOK.getValue(), BookListQueryDTO.class);
        put(ItemType.GOODS.getValue(), GoodsListQueryDTO.class);
        put(ItemType.FIGURE.getValue(), FigureListQueryDTO.class);
    }};

    public static Class<? extends SubItem> getSubClass(int type) {
        return subItemMap.get(type);
    }

    public static Class<? extends SuperItem> getSuperItem(int type) {
        return SuperItemMap.get(type);
    }

    public static Class<? extends ItemListVO> getItemListVO(int type) {
        return itemListVOMap.get(type);
    }

    public static Class<? extends ItemVO> getDetailVO(int type) {
        return itemDetailVOMap.get(type);
    }

    @SneakyThrows
    public static ItemListQueryDTO getItemListQueryDTO(ListQuery qry) {
        Class<? extends ItemListQueryDTO> queryClass = itemListQryMap.get((int)qry.getVal("itemType"));
        Constructor<? extends ItemListQueryDTO> constructor = queryClass.getConstructor(ListQuery.class);
        return constructor.newInstance(qry);
    }

}