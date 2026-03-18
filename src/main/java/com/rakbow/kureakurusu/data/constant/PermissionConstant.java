package com.rakbow.kureakurusu.data.constant;

/**
 * @author Rakbow
 * @since 2026/3/17 0:22
 */
public interface PermissionConstant {

    String ADMIN = "admin";

    String ENTRY_QUERY_LIST = "entity:entry:list";
    String ENTRY_CREATE = "entity:entry:add";
    String ENTRY_UPDATE = "entity:entry:update";
    String ENTRY_DELETE = "entity:entry:delete";
    String ENTRY_UPLOAD_IMAGE = "entity:entry:image";

    String ITEM_QUERY_LIST = "entity:item:list";
    String ITEM_CREATE = "entity:item:add";
    String ITEM_UPDATE = "entity:item:update";
    String ITEM_DELETE = "entity:item:delete";

    String INDEX_CREATE = "entity:index:add";
    String INDEX_ADD_ITEM = "entity:index:add-item";

    String IMAGE_UPLOAD = "resource:image:upload";
    String IMAGE_DELETE = "resource:image:delete";
    String IMAGE_UPDATE = "resource:image:update";

    String FILE_LOCAL_PATH = "resource:file:local-path";
    String FILE_LOCAL_FLAG_UPDATE = "resource:file:local-flag-update";

    String ENTITY_STATUS_UPDATE = "entity:status:update";
    String ENTITY_DETAIL_UPDATE = "entity:detail:update";

    String ROLE_QUERY_LIST = "entity:role:list";
    String ROLE_CREATE = "entity:role:add";
    String ROLE_UPDATE = "entity:role:update";
    String ROLE_REFRESH = "entity:role:refresh";

    String RELATION_QUERY_LIST = "entity:relation:list";
    String RELATION_CREATE = "entity:relation:add";
    String RELATION_UPDATE = "entity:relation:update";
    String RELATION_DELETE = "entity:relation:delete";


}
