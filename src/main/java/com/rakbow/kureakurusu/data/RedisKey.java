package com.rakbow.kureakurusu.data;

/**
 * redis缓存常量
 *
 * @author Rakbow
 * @since 2023-02-17 18:53
 */
public class RedisKey {

    public static final String ENTITY_TOTAL_COUNT = "entity_total_count:%s";
    public static final String ENTITY_IMAGE_CACHE = "entity_image_cache:";


    //popular
    public static final String ENTRY_POPULAR_RANK = "entry_popular_rank";
    public static final String ITEM_POPULAR_RANK = "item_popular_rank";
    public static final String EPISODE_POPULAR_RANK = "ep_popular_rank";
    public static final String PRODUCT_POPULAR_RANK = "product_popular_rank";
    public static final String PERSON_POPULAR_RANK = "person_popular_rank";
    public static final String CHARACTER_POPULAR_RANK = "char_popular_rank";
    public static final String CLASSIFICATION_POPULAR_RANK = "class_popular_rank";
    public static final String MATERIAL_POPULAR_RANK = "material_popular_rank";
    public static final String EVENT_POPULAR_RANK = "event_popular_rank";

    //visit
    public static final String PREFIX_VISIT_TOKEN = "visit_token";
    public static final String PREFIX_VISIT = "visit";

}
