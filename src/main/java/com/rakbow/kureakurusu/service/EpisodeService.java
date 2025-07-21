package com.rakbow.kureakurusu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.rakbow.kureakurusu.dao.AlbumDiscMapper;
import com.rakbow.kureakurusu.dao.EpisodeMapper;
import com.rakbow.kureakurusu.dao.ItemMapper;
import com.rakbow.kureakurusu.data.SearchResult;
import com.rakbow.kureakurusu.data.dto.EpisodeListQueryDTO;
import com.rakbow.kureakurusu.data.dto.EpisodeRelatedDTO;
import com.rakbow.kureakurusu.data.emun.EntityType;
import com.rakbow.kureakurusu.data.emun.ImageType;
import com.rakbow.kureakurusu.data.entity.Episode;
import com.rakbow.kureakurusu.data.entity.item.AlbumDisc;
import com.rakbow.kureakurusu.data.entity.item.Item;
import com.rakbow.kureakurusu.data.vo.EntityMiniVO;
import com.rakbow.kureakurusu.data.vo.EntityRelatedCount;
import com.rakbow.kureakurusu.data.vo.episode.EpisodeListVO;
import com.rakbow.kureakurusu.data.vo.episode.EpisodeRelatedVO;
import com.rakbow.kureakurusu.data.vo.episode.EpisodeVO;
import com.rakbow.kureakurusu.exception.ErrorFactory;
import com.rakbow.kureakurusu.toolkit.CommonUtil;
import com.rakbow.kureakurusu.toolkit.EntityUtil;
import com.rakbow.kureakurusu.toolkit.PopularUtil;
import io.github.linpeilie.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Rakbow
 * @since 2024/01/08 16:38
 */
@Service
@RequiredArgsConstructor
public class EpisodeService extends ServiceImpl<EpisodeMapper, Episode> {

    private final Converter converter;
    private final EntityUtil entityUtil;
    private final PopularUtil popularUtil;

    private final ImageService imageSrv;
    private final FileService fileSrv;

    private final AlbumDiscMapper discMapper;
    private final ItemMapper itemMapper;
    private final EntityType ENTITY_TYPE = EntityType.EPISODE;

    @Transactional
    @SneakyThrows
    public EpisodeVO detail(long id) {
        Episode ep = getById(id);
        if (ep == null) throw ErrorFactory.entityNull();
        EpisodeVO vo = converter.convert(ep, EpisodeVO.class);
        int relatedType = ep.getRelatedType();
        long relatedId = ep.getRelatedId();
        if (ep.getRelatedType() == EntityType.ALBUM_DISC.getValue()) {
            AlbumDisc disc = discMapper.selectById(ep.getRelatedId());
            if (disc == null) throw ErrorFactory.entityNull();
            vo.setDiscNo(disc.getDiscNo());
            relatedType = EntityType.ITEM.getValue();
            relatedId = disc.getItemId();
        }
        vo.setTraffic(entityUtil.buildTraffic(EntityType.EPISODE.getValue(), id));
        vo.setCover(imageSrv.getCache(relatedType, relatedId, ImageType.MAIN));

        //update popularity
        popularUtil.updateEntityPopularity(ENTITY_TYPE.getValue(), id);

        return vo;
    }

    @Transactional
    @SneakyThrows
    public SearchResult<EpisodeListVO> list(EpisodeListQueryDTO dto) {
        dto.init();
        MPJLambdaWrapper<Episode> wrapper = new MPJLambdaWrapper<Episode>()
                .orderBy(dto.isSort(), dto.asc(), dto.getSortField())
                .orderByDesc(!dto.isSort(), Episode::getId);
        if (dto.getKeyword() != null && !dto.getKeyword().isEmpty()) {
            wrapper.like(Episode::getName, dto.getKeyword()).or().like(Episode::getNameEn, dto.getKeyword());
        }
        long start = System.currentTimeMillis();
        IPage<Episode> pages = page(new Page<>(dto.getPage(), dto.getSize()), wrapper);
        if(pages.getRecords().isEmpty()) return new SearchResult<>();
        List<EpisodeListVO> res = converter.convert(pages.getRecords(), EpisodeListVO.class);

        //get album info
        List<Long> discIds = pages.getRecords().stream().map(Episode::getRelatedId).distinct().toList();
        List<AlbumDisc> discs = discMapper.selectByIds(discIds);
        List<Long> itemIds = discs.stream().map(AlbumDisc::getItemId).distinct().toList();
        List<Item> items = itemMapper.selectByIds(itemIds);


        res.forEach(e -> discs.stream()
                .filter(d -> d.getId() == e.getRelatedId())
                .findFirst().ifPresent(d -> {
                    // 1. 先找到对应的item并设置parent
                    items.stream()
                            .filter(i -> i.getId() == d.getItemId())
                            .findFirst()
                            .ifPresent(e::setParent);
                    // 2. 然后可以进行其他赋值操作
                    e.setDiscNo(d.getDiscNo());
                }));

        //get file count
        List<Long> ids = res.stream().map(EpisodeListVO::getId).toList();
        Map<Long, Integer> fileCountMap = fileSrv.count(ENTITY_TYPE.getValue(), ids).stream()
                .collect(Collectors.toMap(EntityRelatedCount::getEntityId, EntityRelatedCount::getCount));
        for (EpisodeListVO ep : res) {
            ep.setFileCount(fileCountMap.getOrDefault(ep.getId(), 0));
        }

        return new SearchResult<>(res, pages.getTotal(), start);
    }

    @Transactional
    @SneakyThrows
    public EpisodeRelatedVO related(EpisodeRelatedDTO dto) {
        EpisodeRelatedVO res = new EpisodeRelatedVO();
        if (dto.getRelatedType() == EntityType.ALBUM_DISC.getValue()) {

            AlbumDisc disc = discMapper.selectById(dto.getRelatedId());
            if (disc == null) throw ErrorFactory.entityNull();
            Item album = itemMapper.selectById(disc.getItemId());
            res.setParent(
                    EntityMiniVO.builder()
                            .type(EntityType.ITEM.getValue())
                            .id(album.getId())
                            .name(album.getName())
                            .subName(STR."\{album.getReleaseDate()}  \{album.getCatalogId()}")
                            .tableName(EntityType.ITEM.getTableName())
                            .thumb(imageSrv.getCache(EntityType.ITEM.getValue(), disc.getItemId(), ImageType.THUMB))
                            .build()
            );
        }
        List<Episode> allEps = list(
                new LambdaQueryWrapper<Episode>()
                        .eq(Episode::getRelatedType, dto.getRelatedType())
                        .eq(Episode::getRelatedId, dto.getRelatedId())
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
