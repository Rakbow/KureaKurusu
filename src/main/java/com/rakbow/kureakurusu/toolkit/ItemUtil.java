package com.rakbow.kureakurusu.toolkit;

import com.rakbow.kureakurusu.data.enums.EntryType;
import com.rakbow.kureakurusu.data.enums.ItemType;
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
        put(ItemType.VIDEO.getValue(), Video.class);
        put(ItemType.GOODS.getValue(), Goods.class);
        put(ItemType.FIGURE.getValue(), Figure.class);
    }};

    private final static Map<Integer, Class<? extends ItemListVO>> itemListVOMap = new HashMap<>() {{
        put(ItemType.ALBUM.getValue(), AlbumListVO.class);
        put(ItemType.BOOK.getValue(), BookListVO.class);
        put(ItemType.VIDEO.getValue(), VideoListVO.class);
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

    public static boolean isValidISBN10(String isbn) {
        if (isbn == null) {
            return false;
        }

        // 去除常见分隔符
        isbn = isbn.replace("-", "").replace(" ", "");

        if (isbn.length() != 10) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < 10; i++) {
            char c = isbn.charAt(i);

            int value;
            if (i == 9 && (c == 'X' || c == 'x')) {
                value = 10;
            } else if (Character.isDigit(c)) {
                value = c - '0';
            } else {
                return false;
            }

            sum += value * (10 - i);
        }

        return sum % 11 == 0;
    }

    public static String convertToISBN13(String isbn10) {
        if (!isValidISBN10(isbn10)) {
            throw new IllegalArgumentException("Invalid ISBN-10");
        }

        isbn10 = isbn10.replace("-", "").replace(" ", "");

        // 978 + 前9位
        String base = STR."978\{isbn10.substring(0, 9)}";

        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = base.charAt(i) - '0';
            sum += (i % 2 == 0) ? digit : digit * 3;
        }

        int checkDigit = (10 - (sum % 10)) % 10;
        return base + checkDigit;
    }

}