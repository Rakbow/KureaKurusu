package com.rakbow.kureakurusu.service;

import com.rakbow.kureakurusu.dao.CommonMapper;
import com.rakbow.kureakurusu.data.RedisKey;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.ChangelogListQueryDTO;
import com.rakbow.kureakurusu.data.dto.UpdateDetailDTO;
import com.rakbow.kureakurusu.data.dto.UpdateStatusDTO;
import com.rakbow.kureakurusu.data.entity.Link;
import com.rakbow.kureakurusu.data.enums.*;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.data.meta.MetaOption;
import com.rakbow.kureakurusu.data.vo.ChangelogMiniVO;
import com.rakbow.kureakurusu.data.vo.ChangelogVO;
import com.rakbow.kureakurusu.toolkit.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Rakbow
 * @since 2023-05-19 18:56
 */
@RequiredArgsConstructor
@Transactional
@Service
public class GeneralService {

    private final LikeUtil likeUtil;
    private final RedisUtil redisUtil;

    private final CommonMapper mapper;
    private final LinkService lnkSrv;

    private final ChangelogService logSrv;

    /**
     * 刷新redis中的选项缓存
     *
     * @author rakbow
     */
    // public void refreshRedisEnumData() {
    //
    //     Map<String, List<Attribute<Integer>>> enumOptionsRedisKeyPair = EnumUtil.getOptionRedisKeyPair();
    //     enumOptionsRedisKeyPair.forEach(redisUtil::set);
    //
    // }

    //region common
    public void loadMetaData() {
        MetaData.optionsZh = new MetaOption();
        MetaData.optionsEn = new MetaOption();

        MetaData.optionsZh.albumFormatSet = EnumHelper.getAttributeOptions(AlbumFormat.class, "zh");
        MetaData.optionsEn.albumFormatSet = EnumHelper.getAttributeOptions(AlbumFormat.class, "en");

        MetaData.optionsZh.mediaFormatSet = EnumHelper.getAttributeOptions(MediaFormat.class, "zh");
        MetaData.optionsEn.mediaFormatSet = EnumHelper.getAttributeOptions(MediaFormat.class, "en");


        if(redisUtil.hasKey(STR."\{RedisKey.OPTION_ROLE_SET}:zh")) {
            String roleSetZhJson = JsonUtil.toJson(redisUtil.get(STR."\{RedisKey.OPTION_ROLE_SET}:zh"));
            MetaData.optionsZh.roleSet = JsonUtil.toAttributes(roleSetZhJson, Long.class);
        }
        if(redisUtil.hasKey(STR."\{RedisKey.OPTION_ROLE_SET}:en")) {
            String roleSetEnJson = JsonUtil.toJson(redisUtil.get(STR."\{RedisKey.OPTION_ROLE_SET}:en"));
            MetaData.optionsEn.roleSet = JsonUtil.toAttributes(roleSetEnJson, Long.class);
        }
    }

    /**
     * 批量更新数据库实体激活状态
     * @author rakbow
     */
    public void updateEntityStatus(UpdateStatusDTO dto) {
        mapper.updateEntityStatus(EntityType.getTableName(dto.getEntity()), dto.getIds(), dto.status());
    }

    /**
     * 点赞
     * @author rakbow
     */
    public boolean like(int entityType, long entityId, String likeToken) {
        //点过赞
        if (likeUtil.isLike(entityType, entityId, likeToken)) {
            return false;
        } else {
            //没点过赞,自增
            likeUtil.inc(entityType, entityId, likeToken);
            //更新热度
            // popularUtil.updateEntityPopularity(entityType, entityId);
            return true;
        }
    }

    /**
     * 更新描述
     *
     * @author rakbow
     */
    @SneakyThrows
    public void updateEntityDetail(UpdateDetailDTO dto) {
        mapper.updateEntityDetail(EntityType.getTableName(dto.getEntityType()), dto.getEntityId(), dto.getText(), DateHelper.now());

        logSrv.create(dto.getEntityType(), dto.getEntityId(), ChangelogField.DETAIL, ChangelogOperate.UPDATE);
    }

    public Map<String, Object> getOptions() {
        Map<String, Object> res = new HashMap<>();
        res.put("albumFormatSet", Objects.requireNonNull(MetaData.getOptions()).albumFormatSet);
        res.put("mediaFormatSet", Objects.requireNonNull(MetaData.getOptions()).mediaFormatSet);
        res.put("roleSet", Objects.requireNonNull(MetaData.getOptions()).roleSet);
        return res;
    }

    public SearchResult<ChangelogVO> changelogs(ChangelogListQueryDTO dto) {
        return logSrv.list(dto);
    }

    public ChangelogMiniVO mini(int type, long id) {
        return logSrv.mini(type, id);
    }

    public List<Link> links(int entityType, long entityId) {
        return lnkSrv.list(entityType, entityId);
    }

}
