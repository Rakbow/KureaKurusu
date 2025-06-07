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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        put(ItemType.DISC.getValue(), ItemDisc.class);
        put(ItemType.GOODS.getValue(), ItemGoods.class);
        put(ItemType.FIGURE.getValue(), ItemFigure.class);
    }};
    private final static Map<Integer, Class<? extends SuperItem>> SuperItemMap = new HashMap<>() {{
        put(ItemType.ALBUM.getValue(), Album.class);
        put(ItemType.BOOK.getValue(), Book.class);
        put(ItemType.DISC.getValue(), Disc.class);
        put(ItemType.GOODS.getValue(), Goods.class);
        put(ItemType.FIGURE.getValue(), Figure.class);
    }};

    private final static Map<Integer, Class<? extends ItemListVO>> itemListVOMap = new HashMap<>() {{
        put(ItemType.ALBUM.getValue(), AlbumListVO.class);
        put(ItemType.BOOK.getValue(), BookListVO.class);
        put(ItemType.DISC.getValue(), DiscListVO.class);
        put(ItemType.GOODS.getValue(), GoodsListVO.class);
        put(ItemType.FIGURE.getValue(), FigureListVO.class);
    }};

    private final static Map<Integer, Class<? extends ItemVO>> itemDetailVOMap = new HashMap<>() {{
        put(ItemType.ALBUM.getValue(), AlbumVO.class);
        put(ItemType.BOOK.getValue(), BookVO.class);
        put(ItemType.DISC.getValue(), DiscVO.class);
        put(ItemType.GOODS.getValue(), GoodsVO.class);
        put(ItemType.FIGURE.getValue(), FigureVO.class);
    }};

    private final static Map<Integer, Class<? extends ItemListQueryDTO>> itemListQryMap = new HashMap<>() {{
        put(ItemType.ALBUM.getValue(), AlbumListQueryDTO.class);
        put(ItemType.BOOK.getValue(), BookListQueryDTO.class);
        put(ItemType.DISC.getValue(), DiscListQueryDTO.class);
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

    public static List<String> expandAlbumRange(String rawCode) {
        if(!rawCode.contains("~")) return List.of(rawCode);

        // 正则提取前缀、起始编号、结束编号
        Pattern pattern = Pattern.compile("^(.*?-)(\\d+)(?:~(\\d+))?$");
        Matcher matcher = pattern.matcher(rawCode);

        if (!matcher.matches()) {
            throw new IllegalArgumentException(STR."格式不正确: \{rawCode}");
        }

        String prefix = matcher.group(1);
        String startStr = matcher.group(2);
        String endStr = matcher.group(3);

        int startNum = Integer.parseInt(startStr);
        int endNum;

        if (endStr != null) {
            // 补齐末尾位数：如果 endStr 是两位，而 start 是四位（如 4539~41），补成 4541
            String fullEndStr = startStr.substring(0, startStr.length() - endStr.length()) + endStr;
            endNum = Integer.parseInt(fullEndStr);
        } else {
            // 没有 ~ 表示只有一张碟片
            endNum = startNum;
        }

        int width = startStr.length(); // 保留前导 0 的宽度
        List<String> result = new ArrayList<>();

        for (int i = startNum; i <= endNum; i++) {
            String numStr = String.format(STR."%0\{width}d", i); // 保留前导0
            result.add(prefix + numStr);
        }

        return result;
    }

}