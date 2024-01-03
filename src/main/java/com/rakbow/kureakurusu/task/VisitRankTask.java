package com.rakbow.kureakurusu.task;

import com.rakbow.kureakurusu.data.common.Visit;
import com.rakbow.kureakurusu.util.common.RedisUtil;
import com.rakbow.kureakurusu.util.common.VisitUtil;
import com.rakbow.kureakurusu.util.convertMapper.entity.StatisticPOMapper;
import org.jetbrains.annotations.NotNull;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 每1小时更新一次，将浏览数排名前十更新
 *
 * @author Rakbow
 * @since 2023-02-23 10:36
 */
@Component
public class VisitRankTask extends QuartzJobBean {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private VisitUtil visitUtil;

    private final StatisticPOMapper statisticPOMapper = StatisticPOMapper.INSTANCES;

    @Override
    protected void executeInternal(@NotNull JobExecutionContext context) {
        // 定时任务逻辑
        // TODO
        System.out.println("------redis缓存数据获取中------");
        //获取所有浏览数据
        List<String> visitKeys = redisUtil.keys(VisitUtil.PREFIX_VISIT + VisitUtil.SPLIT + "*");
        System.out.println("------redis缓存数据获取完毕------");

        System.out.println("------缓存数据转换中------");
        List<Visit> visits = statisticPOMapper.keys2Visit(visitKeys);
        System.out.println("------缓存数据转换完毕------");

        System.out.println("------正在更新浏览排名------");
        //清空浏览数据
        visitUtil.clearAllVisitRank();
        //更新数据
        visits.forEach(visit -> {
            visitUtil.setEntityVisitRanking(visit.getEntityType(), visit.getEntityId(), visit.getVisitCount());
        });
        System.out.println("------浏览排名更新完毕------");
    }

}