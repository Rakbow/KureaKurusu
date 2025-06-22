package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rakbow.kureakurusu.dao.EpisodeMapper;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.EpisodeListQueryDTO;
import com.rakbow.kureakurusu.data.dto.EpisodeRelatedDTO;
import com.rakbow.kureakurusu.data.dto.ListQuery;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.emun.ImageType;
import com.rakbow.kureakurusu.data.entity.Episode;
import com.rakbow.kureakurusu.data.entity.item.Album;
import com.rakbow.kureakurusu.data.entity.item.Item;
import com.rakbow.kureakurusu.data.vo.EntityMiniVO;
import com.rakbow.kureakurusu.data.vo.episode.EpisodeListVO;
import com.rakbow.kureakurusu.data.vo.episode.EpisodeRelatedVO;
import com.rakbow.kureakurusu.data.vo.episode.EpisodeVO;
import com.rakbow.kureakurusu.exception.ErrorFactory;
import com.rakbow.kureakurusu.toolkit.*;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * @author Rakbow
 * @since 2024/01/08 16:38
 */
@Service
@RequiredArgsConstructor
public class EpisodeService extends ServiceImpl<EpisodeMapper, Episode> {

    private final ItemService itemSrv;
    private final Converter converter;
    private final EntityUtil entityUtil;
    private final PopularUtil popularUtil;
    private final ResourceService resourceSrv;
    private final EntityType ENTITY_TYPE = EntityType.EPISODE;

    @Transactional
    @SneakyThrows
    public EpisodeVO detail(long id) {
        Episode ep = getById(id);
        if (ep == null) throw ErrorFactory.entityNull();
        EpisodeVO vo = converter.convert(ep, EpisodeVO.class);
        vo.setTraffic(entityUtil.buildTraffic(EntityType.EPISODE.getValue(), id));
        vo.setCover(resourceSrv.getEntityImageCache(EntityType.ITEM.getValue(), ep.getRelatedId(), ImageType.MAIN));

        //update popularity
        popularUtil.updateEntityPopularity(ENTITY_TYPE.getValue(), id);

        return vo;
    }

    @Transactional
    @SneakyThrows
    public SearchResult<EpisodeListVO> list(ListQuery dto) {
        EpisodeListQueryDTO param = new EpisodeListQueryDTO(dto);
        QueryWrapper<Episode> wrapper = new QueryWrapper<Episode>()
                .orderBy(param.isSort(), param.asc(), CommonUtil.camelToUnderline(param.getSortField()));
        if (param.getName() != null && !param.getName().isEmpty()) {
            wrapper.like("name", param.getName()).or().like("name_en", param.getName());
        }
        IPage<Episode> pages = page(new Page<>(param.getPage(), param.getSize()), wrapper);
        List<EpisodeListVO> res = converter.convert(pages.getRecords(), EpisodeListVO.class);

        //get album info
        List<Long> albumIds = pages.getRecords().stream().map(Episode::getRelatedId).distinct().toList();
        List<Item> items = itemSrv.list(new LambdaQueryWrapper<Item>().in(Item::getId, albumIds));
        for (EpisodeListVO e : res) {
            Item item = DataFinder.findItemById(e.getRelatedId(), items);
            if (item == null) continue;
            e.getParent().setId(item.getId());
            e.getParent().setName(item.getName());
            e.getParent().setType(EntityType.ITEM.getValue());
            e.getParent().setTableName(EntityType.ITEM.getTableName());
        }

        return new SearchResult<>(res, pages.getTotal(), pages.getCurrent(), pages.getSize());
    }

    @Transactional
    @SneakyThrows
    public EpisodeRelatedVO related(EpisodeRelatedDTO dto) {
        EpisodeRelatedVO res = new EpisodeRelatedVO();
        if (dto.getRelatedType() == EntityType.ITEM.getValue()) {
            Album album = itemSrv.getById(dto.getRelatedId());
            res.setParent(
                    EntityMiniVO.builder()
                            .type(dto.getRelatedType())
                            .id(dto.getRelatedId())
                            .name(album.getName())
                            .subName(STR."\{album.getReleaseDate()} \{album.getCatalogId()}")
                            .tableName(EntityType.ITEM.getTableName())
                            .thumb(resourceSrv.getEntityImageCache(dto.getRelatedType(), dto.getRelatedId(), ImageType.THUMB))
                            .build()
            );
        }
        List<Episode> allEps = list(
                new LambdaQueryWrapper<Episode>()
                        .eq(Episode::getRelatedType, dto.getRelatedType())
                        .eq(Episode::getRelatedId, dto.getRelatedId())
                        .eq(Episode::getDiscNo, dto.getDiscNo())
                        .orderByAsc(Episode::getSerial)
        );
        List<Episode> nearEps = getNearbyRecords(allEps, dto.getId(), 10);
        res.setEps(converter.convert(nearEps, EpisodeListVO.class));
        return res;
    }

    public List<Episode> getNearbyRecords(List<Episode> list, long targetId, int maxSize) {
        // 找到目标记录在列表中的索引
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == targetId) {
                index = i;
                break;
            }
        }
        if (index == -1) return Collections.emptyList(); // 没有找到目标ID

        int half = maxSize / 2;
        int start = Math.max(0, index - half);
        int end = Math.min(list.size(), start + maxSize);

        // 如果尾部不够，再往前补
        if (end - start < maxSize) {
            start = Math.max(0, end - maxSize);
        }

        return list.subList(start, end);
    }

}
