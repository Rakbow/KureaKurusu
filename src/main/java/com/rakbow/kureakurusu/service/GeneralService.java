package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rakbow.kureakurusu.dao.CommonMapper;
import com.rakbow.kureakurusu.dao.RoleMapper;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.RedisKey;
import com.rakbow.kureakurusu.data.dto.EntityDTO;
import com.rakbow.kureakurusu.data.dto.EntityResourceInfoUpdateDTO;
import com.rakbow.kureakurusu.data.dto.UpdateDetailDTO;
import com.rakbow.kureakurusu.data.dto.UpdateStatusDTO;
import com.rakbow.kureakurusu.data.entity.Role;
import com.rakbow.kureakurusu.data.enums.AlbumFormat;
import com.rakbow.kureakurusu.data.enums.EntityType;
import com.rakbow.kureakurusu.data.enums.MediaFormat;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.data.meta.MetaOption;
import com.rakbow.kureakurusu.data.vo.ChangelogMiniVO;
import com.rakbow.kureakurusu.data.vo.LinksVO;
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
    private final RoleMapper roleMapper;
    private final LinkService lnkSrv;
    private final ResourceService resSrv;

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
    @SuppressWarnings("unchecked")
    public void loadMetaData() {
        MetaData.optionsZh = new MetaOption();
        MetaData.optionsEn = new MetaOption();

        MetaData.optionsZh.albumFormatSet = EnumHelper.getAttributeOptions(AlbumFormat.class, "zh");
        MetaData.optionsEn.albumFormatSet = EnumHelper.getAttributeOptions(AlbumFormat.class, "en");

        MetaData.optionsZh.mediaFormatSet = EnumHelper.getAttributeOptions(MediaFormat.class, "zh");
        MetaData.optionsEn.mediaFormatSet = EnumHelper.getAttributeOptions(MediaFormat.class, "en");

        String roleSetZhKey = STR."\{RedisKey.OPTION_ROLE_SET}:zh";
        String roleSetEnKey = STR."\{RedisKey.OPTION_ROLE_SET}:en";

        if (!redisUtil.hasKey(roleSetZhKey) || !redisUtil.hasKey(roleSetEnKey)) {
            List<Role> roles = roleMapper.selectList(new LambdaQueryWrapper<Role>().orderByAsc(Role::getId));
            roles.forEach(i -> {
                MetaData.optionsZh.roleSet.add(new Attribute<>(i.getNameZh(), i.getId()));
                MetaData.optionsEn.roleSet.add(new Attribute<>(i.getNameEn(), i.getId()));
            });
            redisUtil.delete(roleSetZhKey);
            redisUtil.set(roleSetZhKey, MetaData.optionsZh.roleSet);
            redisUtil.delete(roleSetEnKey);
            redisUtil.set(roleSetEnKey, MetaData.optionsEn.roleSet);
        }

        MetaData.optionsZh.roleSet = JsonUtil.toAttributes(redisUtil.get(roleSetZhKey), Long.class);
        MetaData.optionsEn.roleSet = JsonUtil.toAttributes(redisUtil.get(roleSetEnKey), Long.class);
    }

    /**
     * 批量更新数据库实体激活状态
     *
     * @author rakbow
     */
    public void updateEntityStatus(UpdateStatusDTO dto) {
        mapper.updateEntityStatus(EntityType.getTableName(dto.entity()), dto.ids(), dto.status() ? 1 : 0);
    }

    /**
     * 点赞
     *
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
        mapper.updateEntityDetail(EntityType.getTableName(dto.entityType()), dto.entityId(), dto.text(), DateHelper.now());

        // logSrv.create(dto.getEntityType(), dto.getEntityId(), ChangelogField.DETAIL, ChangelogOperate.UPDATE);
    }

    public Map<String, Object> getOptions() {
        Map<String, Object> res = new HashMap<>();
        res.put("albumFormatSet", Objects.requireNonNull(MetaData.getOptions()).albumFormatSet);
        res.put("mediaFormatSet", Objects.requireNonNull(MetaData.getOptions()).mediaFormatSet);
        res.put("roleSet", Objects.requireNonNull(MetaData.getOptions()).roleSet);
        return res;
    }

    // public SearchResult<ChangelogVO> changelogs(ChangelogListQueryDTO dto) {
    //     return logSrv.list(dto);
    // }

    public ChangelogMiniVO mini(int type, long id) {
        return logSrv.mini(type, id);
    }

    public List<LinksVO> links(int entityType, long entityId) {
        return lnkSrv.group(entityType, entityId);
    }

    public void localPath(EntityDTO dto) {
        resSrv.getLocalPath(dto.entityType(), dto.entitySubType(), dto.entityId());
    }

    public void updateLocalResourceCompletedFlag(EntityResourceInfoUpdateDTO dto) {
        resSrv.updateLocalResourceCompletedFlag(dto.entityType(), dto.entityId(), dto.flag());
    }

}
