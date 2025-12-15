package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.rakbow.kureakurusu.dao.EntityResourceInfoMapper;
import com.rakbow.kureakurusu.dao.ItemMapper;
import com.rakbow.kureakurusu.data.entity.EntityResourceInfo;
import com.rakbow.kureakurusu.data.entity.item.Item;
import com.rakbow.kureakurusu.data.enums.EntityType;
import com.rakbow.kureakurusu.data.enums.ItemType;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author Rakbow
 * @since 2025/12/14 15:58
 */
@Service
@RequiredArgsConstructor
public class ResourceService {

    private final EntityResourceInfoMapper mapper;
    private final ItemMapper itemMapper;
    @Value("${system.path.resource}")
    private String albumRawPath;

    /**
     * create and return local path by type adn id of entity
     */
    @SneakyThrows
    public void getLocalPath(int entityType, int entitySubType, long entityId) {
        if (entityType != EntityType.ITEM.getValue()) throw new Exception("Entity type error");
        if (entitySubType != ItemType.ALBUM.getValue()) throw new Exception("Entity Sub type error");

        EntityResourceInfo info = mapper.selectOne(new LambdaUpdateWrapper<>() {{
            eq(EntityResourceInfo::getEntityType, entityType);
            eq(EntityResourceInfo::getEntityId, entityId);
        }});
        if (Objects.isNull(info)) {
            Item item = itemMapper.selectById(entityId);
            String releasePath = generatePath(item.getReleaseDate());
            String folderName = StringUtils.isNotBlank(item.getCatalogId()) ? item.getCatalogId() : STR."ALBUM-\{item.getId()}";

            info = new EntityResourceInfo();
            info.setEntityType(entityType);
            info.setEntitySubType(entitySubType);
            info.setEntityId(entityId);
            info.setPath(STR."\{releasePath}/\{folderName}");
            mapper.insert(info);

        }
        Path path = Paths.get(STR."\{albumRawPath}\{info.getPath()}")
                .toAbsolutePath()
                .normalize();
        Files.createDirectories(path);
        new ProcessBuilder("explorer.exe", path.toString()).start();
    }

    @SneakyThrows
    private String generatePath(String dateStr) {
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

}
