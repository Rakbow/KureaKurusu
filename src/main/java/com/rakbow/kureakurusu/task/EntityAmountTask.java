package com.rakbow.kureakurusu.task;

/**
 * 每24小时更新一次数据总数到redis缓存中
 *
 * @author Rakbow
 * @since 2023-03-21 1:15
 */
// @Component
// public class EntityAmountTask extends QuartzJobBean {
//
//     @Autowired
//     private RedisUtil redisUtil;
//     @Autowired
//     private CommonMapper commonMapper;
//
//     @Override
//     protected void executeInternal(@NotNull JobExecutionContext context) {
//         // 定时任务逻辑
//         // TODO
//         System.out.println("------数据总数统计中------");
//
// //        for (Entity type : Entity.ENTITY_TYPES) {
// //            redisUtil.set("entity_amount:" + type.getId(), entityMapper.getItemAmount(type.getNameEn().toLowerCase()));
// //        }
//
//         System.out.println("------数据总数统计完毕------");
//     }
// }
