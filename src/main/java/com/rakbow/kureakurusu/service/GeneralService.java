package com.rakbow.kureakurusu.service;

import com.rakbow.kureakurusu.dao.CommonMapper;
import com.rakbow.kureakurusu.dao.RoleMapper;
import com.rakbow.kureakurusu.data.Attribute;
import com.rakbow.kureakurusu.data.dto.UpdateDetailDTO;
import com.rakbow.kureakurusu.data.dto.UpdateStatusDTO;
import com.rakbow.kureakurusu.data.emun.AlbumFormat;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.emun.MediaFormat;
import com.rakbow.kureakurusu.data.entity.Role;
import com.rakbow.kureakurusu.data.meta.MetaData;
import com.rakbow.kureakurusu.data.meta.MetaOption;
import com.rakbow.kureakurusu.toolkit.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.rakbow.kureakurusu.data.common.Constant.SLASH;

/**
 * @author Rakbow
 * @since 2023-05-19 18:56
 */
@RequiredArgsConstructor
@Service
public class GeneralService {

    //region util resource
    private final LikeUtil likeUtil;
    private final PopularUtil popularUtil;
    //endregion

    //region mapper
    private final CommonMapper mapper;
    private final RoleMapper roleMapper;

    //endregion

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
    @Transactional
    public void loadMetaData() {
        MetaData.optionsZh = new MetaOption();
        MetaData.optionsEn = new MetaOption();

        MetaData.optionsZh.albumFormatSet = EnumHelper.getAttributeOptions(AlbumFormat.class, "zh");
        MetaData.optionsEn.albumFormatSet = EnumHelper.getAttributeOptions(AlbumFormat.class, "en");

        MetaData.optionsZh.mediaFormatSet = EnumHelper.getAttributeOptions(MediaFormat.class, "zh");
        MetaData.optionsEn.mediaFormatSet = EnumHelper.getAttributeOptions(MediaFormat.class, "en");

        MetaData.optionsZh.roleSet = getPersonRoleSet();
        MetaData.optionsZh.roleSet.sort(DataSorter.attributesLongValueSorter);
        MetaData.optionsEn.roleSet = MetaData.optionsZh.roleSet;
    }

    /**
     * 批量更新数据库实体激活状态
     *
     * @author rakbow
     */
    @Transactional
    public void updateEntityStatus(UpdateStatusDTO dto) {
        mapper.updateEntityStatus(EntityType.getTableName(dto.getEntity()), dto.getIds(), dto.status());
    }

    /**
     * 点赞实体
     *
     * @param entityType,entityId,likeToken 实体表名,实体id,点赞token
     * @author rakbow
     */
    @Transactional
    public boolean like(int entityType, long entityId, String likeToken) {
        //点过赞
        if (likeUtil.isLike(entityType, entityId, likeToken)) {
            return false;
        } else {
            //没点过赞,自增
            likeUtil.inc(entityType, entityId, likeToken);
            //更新热度
            popularUtil.updateEntityPopularity(entityType, entityId);
            return true;
        }
    }

    /**
     * 更新描述
     *
     * @author rakbow
     */
    @Transactional
    @SneakyThrows
    public void updateEntityDetail(UpdateDetailDTO dto) {
        mapper.updateEntityDetail(EntityType.getTableName(dto.getEntityType()), dto.getEntityId(), dto.getText(), DateHelper.now());
    }

    //endregion

    //region person role
    @Transactional
    public void refreshRoleSet() {
        MetaData.optionsZh.roleSet.clear();
        MetaData.optionsEn.roleSet.clear();
        MetaData.optionsZh.roleSet = getPersonRoleSet();
        MetaData.optionsZh.roleSet.sort(DataSorter.attributesLongValueSorter);
        MetaData.optionsEn.roleSet = MetaData.optionsZh.roleSet;
    }

    private List<Attribute<Long>> getPersonRoleSet() {
        List<Attribute<Long>> res = new ArrayList<>();
        //获取所有role数据
        List<Role> items = roleMapper.selectList(null);
        items.forEach(i -> res.add(new Attribute<>(i.getNameZh() + SLASH + i.getNameEn(), i.getId())));
        return res;
    }
    //endregion

    public Map<String, Object> getOptions() {
        Map<String, Object> res = new HashMap<>();
        res.put("albumFormatSet", Objects.requireNonNull(MetaData.getOptions()).albumFormatSet);
        res.put("mediaFormatSet", Objects.requireNonNull(MetaData.getOptions()).mediaFormatSet);
        res.put("roleSet", Objects.requireNonNull(MetaData.getOptions()).roleSet);
        return res;
    }

}
