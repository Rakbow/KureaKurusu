package com.rakbow.kureakurusu.data.emun.temp;

import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.RedisKey;
import com.rakbow.kureakurusu.data.emun.common.MediaFormat;
import com.rakbow.kureakurusu.data.emun.entity.album.AlbumFormat;
import com.rakbow.kureakurusu.data.emun.entity.album.PublishFormat;
import com.rakbow.kureakurusu.data.emun.entity.book.BookType;
import com.rakbow.kureakurusu.data.emun.entity.game.GamePlatform;
import com.rakbow.kureakurusu.data.emun.entity.game.ReleaseType;
import com.rakbow.kureakurusu.data.emun.entity.product.ProductCategory;
import com.rakbow.kureakurusu.data.emun.entry.EntryCategory;
import com.rakbow.kureakurusu.util.common.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Rakbow
 * @since 2023-05-19 18:56
 */
public class EnumUtil {

    public static final Class[] SUPPORTED_EMUN = {
            MediaFormat.class,
            AlbumFormat.class, PublishFormat.class,
            BookType.class,
            GamePlatform.class, ReleaseType.class,
            ProductCategory.class,
            EntryCategory.class
    };

    public static final Map<Class, String> EMUN_REDIS_KEY_PAIR = Map.ofEntries(
            Map.entry(MediaFormat.class, RedisKey.MEDIA_FORMAT_SET),
            Map.entry(AlbumFormat.class, RedisKey.ALBUM_FORMAT_SET),
            Map.entry(PublishFormat.class, RedisKey.PUBLISH_FORMAT_SET),
            Map.entry(BookType.class, RedisKey.BOOK_TYPE_SET),
            Map.entry(GamePlatform.class, RedisKey.GAME_PLATFORM_SET),
            Map.entry(ReleaseType.class, RedisKey.RELEASE_TYPE_SET),
            Map.entry(ProductCategory.class, RedisKey.PRODUCT_VISIT_RANKING),
            Map.entry(EntryCategory.class, RedisKey.ENTRY_CATEGORY_SET)
    );

    public static <T extends Enum<T>> List<Attribute<Integer>> getAttributes(Class<T> clazz, String idsJson) {
        int[] values = JsonUtil.to(idsJson, int[].class);

        List<Attribute<Integer>> res = new ArrayList<>();

        for(int value : values) {
            Attribute<Integer> attribute = getAttribute(clazz, value);
            if(attribute != null) {
                res.add(attribute);
            }
        }

        return res;
    }

    public static <T extends Enum<T>> Attribute<Integer> getAttribute(Class<T> clazz, int value) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        return Arrays.stream(clazz.getEnumConstants())
                .filter(enumValue -> {
                    try {
                        Method idMethod = clazz.getDeclaredMethod("getId");
                        int currentId = (int) idMethod.invoke(enumValue);;
                        return currentId == value;
                    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        return false;
                    }
                })
                .findFirst()
                .map(enumValue -> {
                    try {
                        Method nameMethod = clazz.getDeclaredMethod("get" + "Name" + StringUtils.capitalize(lang));
                        String name = (String) nameMethod.invoke(enumValue);
                        int id = (int) clazz.getDeclaredMethod("getId").invoke(enumValue);
                        return new Attribute<Integer>(name, id);
                    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .orElse(null);
    }

    public static <T extends Enum<T>> List<Attribute<Integer>> getAttributeOptions(Class<T> clazz) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        List<Attribute<Integer>> attributes = new ArrayList<>();
        for (T enumValue : clazz.getEnumConstants()) {
            try {
                Field idField = clazz.getDeclaredField("id");
                idField.setAccessible(true);
                int id = idField.getInt(enumValue);
                if(lang.equals(Locale.CHINESE.getLanguage())) {
                    attributes.add(new Attribute<>(clazz.getDeclaredField("nameZh").get(enumValue).toString(), id));
                }else if(lang.equals(Locale.ENGLISH.getLanguage())) {
                    attributes.add(new Attribute<>(clazz.getDeclaredField("nameEn").get(enumValue).toString(), id));
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // handle exception
            }
        }
        return attributes;
    }

    public static <T extends Enum<T>> List<Attribute<Integer>> getAttributeOptions(Class<T> clazz, String lang) {
        List<Attribute<Integer>> attributes = new ArrayList<>();
        for (T enumValue : clazz.getEnumConstants()) {
            try {
                Field idField = clazz.getDeclaredField("id");
                idField.setAccessible(true);
                int id = idField.getInt(enumValue);
                if(lang.equals(Locale.CHINESE.getLanguage())) {
                    attributes.add(new Attribute<Integer>(clazz.getDeclaredField("nameZh").get(enumValue).toString(), id));
                }else if(lang.equals(Locale.ENGLISH.getLanguage())) {
                    attributes.add(new Attribute<Integer>(clazz.getDeclaredField("nameEn").get(enumValue).toString(), id));
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // handle exception
            }
        }
        return attributes;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Enum<T>> Map<String, List<Attribute<Integer>>> getOptionRedisKeyPair() {
        Map<String, List<Attribute<Integer>>> res = new HashMap<>();

        Arrays.asList(EnumUtil.SUPPORTED_EMUN).forEach(emunClass -> {

            List<Attribute<Integer>> attributesZh = getAttributeOptions(emunClass, Locale.CHINESE.getLanguage());
            List<Attribute<Integer>> attributesEn = getAttributeOptions(emunClass, Locale.ENGLISH.getLanguage());

            String key = EnumUtil.EMUN_REDIS_KEY_PAIR.get(emunClass);
            String zhKey = String.format(key, Locale.CHINESE.getLanguage());
            String enKey = String.format(key, Locale.ENGLISH.getLanguage());

            res.put(zhKey, attributesZh);
            res.put(enKey, attributesEn);
        });

        return res;
    }

}
