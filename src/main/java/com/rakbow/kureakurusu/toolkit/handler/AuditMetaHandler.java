package com.rakbow.kureakurusu.toolkit.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * @author Rakbow
 * @since 2025/6/12 7:32
 */
@Component
public class AuditMetaHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Timestamp now = currentTimestamp();
        strictInsertFill(metaObject, "addedTime", Timestamp.class, now);
        strictInsertFill(metaObject, "editedTime", Timestamp.class, now);

        // 可以添加操作人信息
        // strictInsertFill(metaObject, "createdBy", String.class, getCurrentUser());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        strictUpdateFill(metaObject, "editedTime", Timestamp.class, currentTimestamp());
        // strictUpdateFill(metaObject, "updatedBy", String.class, getCurrentUser());
    }

    private Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    private String getCurrentUser() {
        // 从安全上下文获取当前用户
        return "system"; // 示例
    }
}
