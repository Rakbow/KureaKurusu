package com.rakbow.kureakurusu.toolkit;

import com.rakbow.kureakurusu.data.emun.EntryType;
import com.rakbow.kureakurusu.data.emun.ItemType;
import com.rakbow.kureakurusu.data.entity.item.*;
import com.rakbow.kureakurusu.data.vo.item.*;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author Rakbow
 * @since 2024/4/26 17:04
 */
public class ItemUtil {

    private static final DecimalFormat DIMENSION_FORMAT = new DecimalFormat("0.#");
    private static final DecimalFormat WEIGHT_FORMAT = new DecimalFormat("0.##");

    public static String generateSpec(double width, double length, double height, double weight) {
        StringBuilder specBuilder = new StringBuilder();

        // 处理尺寸部分
        if (width > 0 && length > 0) {
            // 转换为cm并格式化（保留1位小数）
            String wStr = DIMENSION_FORMAT.format(width / 10);
            String lStr = DIMENSION_FORMAT.format(length / 10);

            if (height > 0) {
                // 三维尺寸
                String hStr = DIMENSION_FORMAT.format(height / 10);
                specBuilder.append(wStr).append(" x ")
                        .append(lStr).append(" x ")
                        .append(hStr).append(" cm");
            } else {
                // 二维尺寸
                specBuilder.append(wStr).append(" x ")
                        .append(lStr).append(" cm");
            }
        }

        // 处理重量部分
        if (weight > 0) {
            if (!specBuilder.isEmpty()) {
                // 已有尺寸部分，添加分隔符
                specBuilder.append("; ");
            }
            specBuilder.append(WEIGHT_FORMAT.format(weight)).append(" g");
        }

        return specBuilder.toString();
    }

    public final static List<Integer> ItemExtraEntitySubTypes = Arrays.asList(
            EntryType.CLASSIFICATION.getValue(),
            EntryType.EVENT.getValue(),
            EntryType.MATERIAL.getValue()
    );

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

    public static Class<? extends SuperItem> getSuperItem(int type) {
        return SuperItemMap.get(type);
    }

    public static Class<? extends ItemListVO> getItemListVO(int type) {
        return itemListVOMap.get(type);
    }

    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

}