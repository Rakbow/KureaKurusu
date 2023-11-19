package com.rakbow.kureakurusu.task;

import com.rakbow.kureakurusu.dao.CommonMapper;
import com.rakbow.kureakurusu.util.common.RedisUtil;
import org.jetbrains.annotations.NotNull;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * @Project_name: kureakurusu
 * @Author: Rakbow
 * @Create: 2023-03-21 1:15
 * @Description: 每24小时更新一次数据总数到redis缓存中
 */
@Component
public class EntityAmountTask extends QuartzJobBean {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private CommonMapper commonMapper;

    @Override
    protected void executeInternal(@NotNull JobExecutionContext context) {
        // 定时任务逻辑
        // TODO
        System.out.println("------数据总数统计中------");

//        for (Entity type : Entity.ENTITY_TYPES) {
//            redisUtil.set("entity_amount:" + type.getId(), entityMapper.getItemAmount(type.getNameEn().toLowerCase()));
//        }

        System.out.println("------数据总数统计完毕------");
    }
}
