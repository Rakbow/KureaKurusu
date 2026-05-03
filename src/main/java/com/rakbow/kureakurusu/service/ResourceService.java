package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.rakbow.kureakurusu.dao.EntityResourceInfoMapper;
import com.rakbow.kureakurusu.dao.ItemMapper;
import com.rakbow.kureakurusu.data.dto.EntityResourceInfoUpdateDTO;
import com.rakbow.kureakurusu.data.entity.EntityResourceInfo;
import com.rakbow.kureakurusu.data.entity.item.Item;
import com.rakbow.kureakurusu.data.enums.EntityResourceType;
import com.rakbow.kureakurusu.data.enums.EntityType;
import com.rakbow.kureakurusu.data.enums.ItemType;
import com.rakbow.kureakurusu.toolkit.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * @author Rakbow
 * @since 2025/12/14 15:58
 */
@Service
@RequiredArgsConstructor
public class ResourceService {

    private final EntityResourceInfoMapper entityResourceInfoMapper;
    private final ItemMapper itemMapper;
    @Value("${system.path.resource}")
    private String albumRawPath;

    /**
     * create and return local path by type adn id of entity
     */
    @SneakyThrows
    public void getLocalPath(int entityType, int entitySubType, long entityId) {
        // windows system only
        String osName = System.getProperty("os.name").toLowerCase();
        if (!osName.contains("windows")) {
            throw new Exception(STR."This feature is only supported on Windows operating system. Current OS: \{osName}");
        }
        
        if (entityType != EntityType.ITEM.getValue()) throw new Exception("Entity type error");
        if (entitySubType != ItemType.ALBUM.getValue()) throw new Exception("Entity Sub type error");

        EntityResourceInfo info = entityResourceInfoMapper.selectOne(new LambdaUpdateWrapper<>() {{
            eq(EntityResourceInfo::getEntityType, entityType);
            eq(EntityResourceInfo::getEntityId, entityId);
            eq(EntityResourceInfo::getType, EntityResourceType.LOCAL.getValue());
        }});
        if (Objects.isNull(info)) {
            Item item = itemMapper.selectById(entityId);
            String releasePath = generateLocalPath(item.getReleaseDate());
            String folderName = StringUtil.isNotBlank(item.getCatalogId()) ? item.getCatalogId() : STR."ALBUM-\{item.getId()}";

            info = new EntityResourceInfo();
            info.setEntityType(entityType);
            info.setEntitySubType(entitySubType);
            info.setEntityId(entityId);
            info.setType(EntityResourceType.LOCAL);
            info.setPath(STR."\{releasePath}/\{folderName}");
            entityResourceInfoMapper.insert(info);

        }
        Path path = Paths.get(STR."\{albumRawPath}\{info.getPath()}")
                .toAbsolutePath()
                .normalize();
        Path ripPath = Paths.get(path.toString().replace("raw", "rip"))
                .toAbsolutePath()
                .normalize();
        Files.createDirectories(path);
        Files.createDirectories(ripPath);
        
        // 异步打开资源管理器
        CompletableFuture.runAsync(() -> {
            try {
                new ProcessBuilder("explorer.exe", path.toString()).start();
            } catch (Exception e) {
                throw new RuntimeException(STR."Failed to open path: \{path}", e);
            }
        });
        
        CompletableFuture.runAsync(() -> {
            try {
                new ProcessBuilder("explorer.exe", ripPath.toString()).start();
            } catch (Exception e) {
                throw new RuntimeException(STR."Failed to open rip path: \{ripPath}", e);
            }
        });
    }

    @SneakyThrows
    private String generateLocalPath(String dateStr) {
        String[] parts = dateStr.split("/");

        String year = parts[0];
        String month = "00";
        String day = null;

        if (parts.length >= 2) {
            month = parts[1];
        }
        if (parts.length >= 3) {
            day = parts[2];
        }

        // 只有年
        if (day == null) {
            return STR."/\{year}/\{month}";
        }

        // 年月日
        return STR."/\{year}/\{month}/\{day}";
    }

    @SneakyThrows
    public void updateResourceFlag(EntityResourceInfoUpdateDTO dto) {
        if (dto.entityType() == EntityType.ITEM.getValue()) {
            itemMapper.update(new LambdaUpdateWrapper<>() {{
                eq(Item::getId, dto.entityId());
                set(Item::getResourceFlag, dto.flag());
            }});
        }
    }

}
