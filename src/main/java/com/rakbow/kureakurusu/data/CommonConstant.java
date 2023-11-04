package com.rakbow.kureakurusu.data;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2022-08-02 0:53
 * @Description:
 */
public class CommonConstant {

    //region ------通用常量------

    public static final String STATUS = "status";
    public static final String JSON_ARRAY_SEARCH_FORMAT = "JSON_CONTAINS(%s, ?)";

    //404图url
    public static final String EMPTY_IMAGE_URL = "https://img.rakbow.com/common/error/404.jpg";
    public static final String EMPTY_IMAGE_WIDTH_URL = "https://img.rakbow.com/common/error/404_width.jpg";

    //实体默认图片
    public static final String DEFAULT_ALBUM_IMAGE_URL = "https://img.rakbow.com/common/entity-default/default_album.png";
    public static final String DEFAULT_BOOK_IMAGE_URL = "https://img.rakbow.com/common/error/404.jpg";
    public static final String DEFAULT_DISC_IMAGE_URL = "https://img.rakbow.com/common/entity-default/default_disc.png";
    public static final String DEFAULT_GAME_IMAGE_URL = "https://img.rakbow.com/common/error/404.jpg";
    public static final String DEFAULT_MERCH_IMAGE_URL = "https://img.rakbow.com/common/error/404.jpg";
    public static final String DEFAULT_PRODUCT_IMAGE_URL = "https://img.rakbow.com/common/error/404.jpg";
    public static final String DEFAULT_FRANCHISE_IMAGE_URL = "https://img.rakbow.com/common/error/404.jpg";

    /**
     * 激活成功
     */
    public static final int ACTIVATION_SUCCESS = 0;

    /**
     * 重复激活
     */
    public static final int ACTIVATION_REPEAT = 1;

    /**
     * 激活失败
     */
    public static final int ACTIVATION_FAILURE = 2;

    /**
     * 默认状态的登录凭证的超时时间 12小时
     */
    public static final int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    /**
     * 记住状态的登录凭证超时时间
     */
    public static final int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 10;

    /**
     * 实体类型: 帖子
     */
    public static final int ENTITY_TYPE_POST = 1;

    /**
     * 实体类型: 评论
     */
    public static final int ENTITY_TYPE_COMMENT = 2;

    /**
     * 实体类型: 用户
     */
    public static final int ENTITY_TYPE_USER = 3;

    /**
     * 主题: 评论
     */
    public static final String TOPIC_COMMENT = "comment";

    /**
     * 主题: 点赞
     */
    public static final String TOPIC_LIKE = "like";

    /**
     * 主题: 关注
     */
    public static final String TOPIC_FOLLOW = "follow";

    /**
     * 系统用户ID
     */
    public static final int SYSTEM_USER_ID = 1;

    //region 权限相关
    //权限：普通用户
    public static final String AUTHORITY_USER = "user ";
    //权限：管理员
    public static final String AUTHORITY_ADMIN = "admin";
    //endregion
}
