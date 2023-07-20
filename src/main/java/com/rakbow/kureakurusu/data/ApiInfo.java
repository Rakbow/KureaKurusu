package com.rakbow.kureakurusu.data;

/**
 * @Project_name: database
 * @Author: Rakbow
 * @Create: 2022-09-12 3:43
 * @Description:
 */
public class ApiInfo {

    //region entity common
    public static final String REFRESH_REDIS_DATA_SUCCESS = "Redis缓存数据刷新成功！";
    public static final String NAME_EMPTY = "未填写名称(原)！";
    public static final String NAME_ZH_EMPTY = "未填写名称(中)！";
    public static final String NAME_EN_EMPTY = "未填写名称(英)！";
    public static final String LIKE_SUCCESS = "点赞成功!";
    public static final String LIKE_FAILED = "已点过赞!";
    public static final String NOT_ACTION = "未进行任何操作！";
    public static final String INSERT_DATA_SUCCESS = "%s新增成功！";
    public static final String DELETE_DATA_SUCCESS = "%s删除成功！";
    public static final String UPDATE_DATA_SUCCESS = "%s更新成功！";
    public static final String GET_DATA_FAILED = "%s不存在！";
    public static final String GET_DATA_FAILED_404 = "无效的地址，或者该%s已从数据库中删除。";
    public static final String INPUT_FILE_EMPTY = "文件为空！";
    public static final String INCORRECT_FILE_FORMAT = "文件的格式不正确！";
    public static final String UPLOAD_EXCEPTION = "上传文件失败,服务器发生异常！";
    public static final String GET_IMAGE_FAILED = "读取图片失败: ";
    public static final String GET_FILE_FAILED = "读取文件失败: ";
    public static final String FILE_FORMAT_EXCEPTION = "%s格式错误";
    public static final String FILE_FORMAT_UNSUPPORTED_EXCEPTION = "不支持的%s格式";
    public static final String UPDATE_IMAGES_SUCCESS = "图片更改成功！";
    public static final String INSERT_IMAGES_SUCCESS = "图片新增成功！";
    public static final String DELETE_IMAGES_SUCCESS = "图片删除成功！";
    public static final String COVER_COUNT_EXCEPTION = "只允许一张图片类型为封面！";
    public static final String IMAGE_NAME_EN_REPEAT_EXCEPTION = "图片英文名不能重复！";
    public static final String INPUT_TEXT_EMPTY = "输入内容为空!";
    public static final String FRANCHISES_EMPTY = "未选择所属系列！";
    public static final String PRODUCTS_EMPTY = "未选择所属作品！";
    public static final String UPDATE_DESCRIPTION_SUCCESS = "描述更新成功！";
    public static final String UPDATE_BONUS_SUCCESS = "特典信息更新成功！";
    public static final String UPDATE_SPEC_SUCCESS = "规格信息更新成功！";
    public static final String UPDATE_COMPANIES_SUCCESS = "关联企业信息更新成功！";
    public static final String UPDATE_PERSONNEL_SUCCESS = "相关人员信息更新成功！";

    //endregion

    //region 登录相关
    public static final String INCORRECT_VERIFY_CODE = "验证码不正确!";
    public static final String USERNAME_ARE_EMPTY = "账号不能为空!";
    public static final String PASSWORD_ARE_EMPTY = "密码不能为空!";
    public static final String USER_NOT_EXIST = "该账号不存在!";
    public static final String USER_ARE_INACTIVATED = "该账号未激活!";
    public static final String INCORRECT_PASSWORD = "密码不正确!";
    //endregion

    //region 权限相关
    public static final String NOT_LOGIN = "未登录!";
    public static final String NOT_AUTHORITY = "当前用户无权限!";
    public static final String NOT_AUTHORITY_DENIED = "当前用户无权限访问此功能！";
    //endregion

    //region 专辑相关
     public static final String UPDATE_ALBUM_TRACK_INFO_SUCCESS = "专辑音轨信息更新成功！";
     public static final String UPDATE_ALBUM_ARTISTS_SUCCESS = "专辑创作者信息更新成功！";
     public static final String ALBUM_NAME_EMPTY = "未填写专辑名称！";
     public static final String ALBUM_RELEASE_DATE_EMPTY = "未填写发行日期！";
     public static final String ALBUM_PUBLISH_FORMAT_EMPTY = "未选择出版形式！";
     public static final String ALBUM_ALBUM_FORMAT_EMPTY = "未选择专辑分类！";
     public static final String ALBUM_MEDIA_FORMAT_EMPTY = "未选择媒体格式！";

    //endregion

    //region music相关

    public static final String UPDATE_MUSIC_ARTISTS_SUCCESS = "创作人员信息更新成功！";
    public static final String UPDATE_MUSIC_LYRICS_SUCCESS = "歌词文本更新成功！";

    public static final String MUSIC_NAME_EMPTY = "未填写曲名！";
    public static final String MUSIC_AUDIO_TYPE_EMPTY = "未选择音频类型！";
    public static final String MUSIC_AUDIO_LENGTH_EMPTY = "未填写音频长度！";

    public static final String MUSIC_FILE_NUMBER_EXCEPTION = "只允许上传一个音频文件和一个歌词文件！";

    //endregion

    //region product相关
    public static final String PRODUCT_NAME_EMPTY = "未填写作品名称！";
    public static final String PRODUCT_NAME_ZH_EMPTY = "未填写作品译名(中)！";
    public static final String PRODUCT_RELEASE_DATE_EMPTY = "未填写发行日期！";
    public static final String PRODUCT_FRANCHISE_EMPTY = "未选择作品所属系列！";
    public static final String PRODUCT_CATEGORY_EMPTY = "未选择作品分类！";
    public static final String UPDATE_PRODUCT_ORGANIZATIONS_SUCCESS = "相关组织信息更新成功！";
    public static final String UPDATE_PRODUCT_STAFFS_SUCCESS = "staff信息更新成功！";
    //endregion

    //region disc相关
    public static final String DISC_NAME_EMPTY = "未填写专辑名称！";
    public static final String DISC_RELEASE_DATE_EMPTY = "未填写发行日期！";
    public static final String DISC_MEDIA_FORMAT_EMPTY = "未选择媒体格式！";
    public static final String DISC_REGION_EMPTY = "未选择发行地区！";
    //endregion

    //region book相关
    public static final String UPDATE_BOOK_AUTHOR_SUCCESS = "图书作者信息更新成功！";
    public static final String BOOK_TITLE_EMPTY = "未填写图书名称！";
    public static final String BOOK_ISBN10_LENGTH_EXCEPTION = "ISBN-10无效！";
    public static final String BOOK_ISBN13_LENGTH_EXCEPTION = "ISBN-13无效！";
    public static final String BOOK_TYPE_EMPTY = "未选择图书所属分类！";
    public static final String BOOK_PUBLISH_DATE_EMPTY = "未填写出版日期！";

    //endregion

    //region merch相关
    public static final String MERCH_NAME_EMPTY = "未填写周边名称！";
    public static final String MERCH_CATEGORY_EMPTY = "未选择图书所属分类！";
    public static final String MERCH_RELEASE_DATE_EMPTY = "未填写发售日期！";

    //endregion

    //region game相关
    public static final String UPDATE_GAME_ORGANIZATIONS_SUCCESS = "相关组织信息更新成功！";
    public static final String UPDATE_GAME_STAFFS_SUCCESS = "开发制作人员信息更新成功！";
    public static final String GAME_NAME_EMPTY = "未填写游戏名称！";
    public static final String GAME_RELEASE_DATE_EMPTY = "未填写游戏发售日期！";
    public static final String GAME_RELEASE_TYPE_EMPTY = "未选择游戏发售类型！";
    public static final String GAME_PLATFORM_EMPTY = "未选择游戏所属平台！";
    public static final String GAME_REGION_EMPTY = "未选择发售地区！";

    //endregion

    //region entry

    public static final String ENTRY_CATEGORY_EMPTY = "未选择分类！";

    //endregion

    //region franchise相关
    public static final String FRANCHISE_NAME_EMPTY = "未填写系列名称！";
    public static final String FRANCHISE_ORIGIN_DATE_EMPTY = "未填写系列起始日期！";

    //endregion

    //region 七牛云
    public static final String QINIU_EXCEPTION = "七牛云异常: %s";
    public static final String DELETE_FILES_SUCCESS = "文件删除成功！";
    //endregion

    //region system

    public static final String UPDATE_ITEM_STATUS_URL = "状态更改成功!";

    //endregion

}
