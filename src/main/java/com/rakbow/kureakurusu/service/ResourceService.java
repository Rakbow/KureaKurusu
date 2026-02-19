package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.rakbow.kureakurusu.dao.EntityResourceInfoMapper;
import com.rakbow.kureakurusu.dao.ItemMapper;
import com.rakbow.kureakurusu.data.entity.EntityResourceInfo;
import com.rakbow.kureakurusu.data.entity.item.Item;
import com.rakbow.kureakurusu.data.enums.EntityType;
import com.rakbow.kureakurusu.data.enums.ItemType;
import com.rakbow.kureakurusu.data.vo.item.ItemSearchVO;
import com.rakbow.kureakurusu.toolkit.EntityUtil;
import com.rakbow.kureakurusu.toolkit.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

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
        if (entityType != EntityType.ITEM.getValue()) throw new Exception("Entity type error");
        if (entitySubType != ItemType.ALBUM.getValue()) throw new Exception("Entity Sub type error");

        EntityResourceInfo info = entityResourceInfoMapper.selectOne(new LambdaUpdateWrapper<>() {{
            eq(EntityResourceInfo::getEntityType, entityType);
            eq(EntityResourceInfo::getEntityId, entityId);
        }});
        if (Objects.isNull(info)) {
            Item item = itemMapper.selectById(entityId);
            String releasePath = generateLocalPath(item.getReleaseDate());
            String folderName = StringUtil.isNotBlank(item.getCatalogId()) ? item.getCatalogId() : STR."ALBUM-\{item.getId()}";

            info = new EntityResourceInfo();
            info.setEntityType(entityType);
            info.setEntitySubType(entitySubType);
            info.setEntityId(entityId);
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
        new ProcessBuilder("explorer.exe", path.toString()).start();
        new ProcessBuilder("explorer.exe", ripPath.toString()).start();
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
    public void getLocalResourceCompletedFlag(List<ItemSearchVO> items) {
        List<Long> ids = items.stream().map(ItemSearchVO::getId).toList();
        List<EntityResourceInfo> infos = entityResourceInfoMapper.selectList(new LambdaUpdateWrapper<>() {{
            eq(EntityResourceInfo::getEntityType, EntityType.ITEM.getValue());
            in(EntityResourceInfo::getEntityId, ids);
        }});
        EntityUtil.matchAndAssign(
                infos,
                items,
                info -> info.getEntityType().intValue() == EntityType.ITEM.getValue(),
                EntityResourceInfo::getEntityId,
                ItemSearchVO::getId,
                (info, item) -> item.setCompletedFlag(info.getCompletedFlag())
        );
    }

    @SneakyThrows
    public void updateLocalResourceCompletedFlag(int entityType, long entityId, int flag) {
        entityResourceInfoMapper.update(new LambdaUpdateWrapper<>() {{
            eq(EntityResourceInfo::getEntityType, entityType);
            eq(EntityResourceInfo::getEntityId, entityId);
            set(EntityResourceInfo::getCompletedFlag, flag);
        }});
    }

}
