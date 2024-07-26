package com.rakbow.kureakurusu.toolkit;

import com.rakbow.kureakurusu.data.dto.*;
import com.rakbow.kureakurusu.data.emun.ItemType;
import com.rakbow.kureakurusu.data.entity.*;
import com.rakbow.kureakurusu.data.entity.common.SuperItem;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.data.vo.item.*;
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
        }else if(type == ItemType.GOODS.getValue()) {
            res.put("goodsTypeSet", Objects.requireNonNull(MetaData.getOptions()).goodsTypeSet);
        }else if(type == ItemType.FIGURE.getValue()) {
            res.put("figureTypeSet", Objects.requireNonNull(MetaData.getOptions()).figureTypeSet);
        }
        res.put("releaseTypeSet", Objects.requireNonNull(MetaData.getOptions()).releaseTypeSet);
        return res;
    }

}