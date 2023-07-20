package com.rakbow.kureakurusu.data;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-02-17 18:53
 * @Description: redis缓存常量
 */
public class RedisKey {

    //region common
    public static final String SPLIT = ":";
    private static final String VISIT_RANK = "visit_rank";
    private static final String COMMON_OPTION = "options:%s:common:";

    private static final String MEDIA_OPTION = "options:%s:media:";
    private static final String MERCH_OPTION = "options:%s:merch:";
    //endregion

    //region options

    //common
    public static final String LANGUAGE_SET = COMMON_OPTION + "LANGUAGE";
    public static final String REGION_SET = COMMON_OPTION + "REGION";
    public static final String MEDIA_FORMAT_SET = COMMON_OPTION + "MEDIA_FORMAT";
    public static final String COMPANY_ROLE_SET = COMMON_OPTION + "COMPANY_ROLE";
    public static final String ENTRY_CATEGORY_SET = COMMON_OPTION + "ENTRY_CATEGORY";

    public static final String FRANCHISE_SET = COMMON_OPTION + "FRANCHISE";
    public static final String CLASSIFICATION_SET = COMMON_OPTION + "CLASSIFICATION";
    public static final String COMPANY_SET = COMMON_OPTION + "COMPANY";
    public static final String PERSONNEL_SET = COMMON_OPTION + "PERSONNEL";
    public static final String MERCH_TYPE_SET = COMMON_OPTION + "MERCH_TYPE";
    public static final String ROLE_SET = COMMON_OPTION + "ROLE";
    public static final String PUBLICATION_SET = COMMON_OPTION + "PUBLICATION";
    public static final String SPEC_PARAM_SET = COMMON_OPTION + "SPEC_PARAM";

    //album
    public static final String ALBUM_FORMAT_SET = MEDIA_OPTION + "ALBUM_FORMAT";
    public static final String PUBLISH_FORMAT_SET = MEDIA_OPTION + "PUBLISH_FORMAT";

    //book
    public static final String BOOK_TYPE_SET = MEDIA_OPTION + "BOOK_TYPE";

    //game
    public static final String RELEASE_TYPE_SET = MEDIA_OPTION + "RELEASE_TYPE";
    public static final String GAME_PLATFORM_SET = MEDIA_OPTION + "GAME_PLATFORM";

    //music
    public static final String AUDIO_TYPE_SET = MEDIA_OPTION + "AUDIO_TYPE";

    //endregion

    //region ranking

    public static final String ALBUM_VISIT_RANKING = VISIT_RANK + SPLIT + "ALBUM";
    public static final String BOOK_VISIT_RANKING = VISIT_RANK + SPLIT + "BOOK";
    public static final String DISC_VISIT_RANKING = VISIT_RANK + SPLIT + "DISC";
    public static final String GAME_VISIT_RANKING = VISIT_RANK + SPLIT + "GAME";
    public static final String MERCH_VISIT_RANKING = VISIT_RANK + SPLIT + "MERCH";
    public static final String MUSIC_VISIT_RANKING = VISIT_RANK + SPLIT + "MUSIC";
    public static final String PRODUCT_VISIT_RANKING = VISIT_RANK + SPLIT + "PRODUCT";
    public static final String FRANCHISE_VISIT_RANKING = VISIT_RANK + SPLIT + "FRANCHISE";

    //endregion

    //region index cover urls

    public static final String INDEX_COVER_URL = "INDEX_COVER_URL";

    //endregion

    //region entity related item

    public static final String ENTITY_RELATED_ITEM = "entity_related_item";

    //endregion

}
