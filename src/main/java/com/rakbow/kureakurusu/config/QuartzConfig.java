package com.rakbow.kureakurusu.config;

import com.rakbow.kureakurusu.task.EntityAmountTask;
import com.rakbow.kureakurusu.task.EntityInfoTask;
import com.rakbow.kureakurusu.task.VisitRankTask;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Rakbow
 * @since 2023-02-22 0:37
 */
@Configuration
public class QuartzConfig {

//     //region 24小时更新数据
//     @Bean
//     public JobDetail entityInfoQuartzDetail(){
//         // withIdentity指定的是这个job的id
//         return JobBuilder
//                 .newJob(EntityInfoTask.class)
//                 .withIdentity("ENTITY_INFO_TASK_IDENTITY")
//                 .storeDurably()
//                 .build();
//     }
//
//     @Bean
//     public Trigger entityInfoQuartzTrigger(){ //触发器
//         SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
//                 // .withIntervalInSeconds(30)  //设置时间周期单位秒
//                .withIntervalInHours(24)  //24小时执行一次
//                 .repeatForever();
//         return TriggerBuilder.newTrigger().forJob(entityInfoQuartzDetail())
// //                .forJob(quartzDetail_2())
//                 .withIdentity("ENTITY_INFO_TRIGGER")
//                 .withSchedule(scheduleBuilder)
//                 .build();
//     }
//     //endregion
//
//     //region 1小时更新浏览排名
//     @Bean
//     public JobDetail visitRankQuartzDetail(){
//         // withIdentity指定的是这个job的id
//         return JobBuilder
//                 .newJob(VisitRankTask.class)
//                 .withIdentity("VISIT_RANK_TASK_IDENTITY")
//                 .storeDurably()
//                 .build();
//     }
//
//     @Bean
//     public Trigger visitRankQuartzTrigger(){ //触发器
//         SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
//                 // .withIntervalInSeconds(30)  //设置时间周期单位秒
//                 .withIntervalInHours(1)  //24小时执行一次
//                 .repeatForever();
//         return TriggerBuilder.newTrigger().forJob(visitRankQuartzDetail())
// //                .forJob(quartzDetail_2())
//                 .withIdentity("VISIT_RANK_INFO_TRIGGER")
//                 .withSchedule(scheduleBuilder)
//                 .build();
//     }
//     //endregion EntityAmountTask

    //region 24小时更新一次数据总数
//     @Bean
//     public JobDetail EntityAmountQuartzDetail(){
//         // withIdentity指定的是这个job的id
//         return JobBuilder
//                 .newJob(EntityAmountTask.class)
//                 .withIdentity("ENTITY_AMOUNT_TASK_IDENTITY")
//                 .storeDurably()
//                 .build();
//     }
//
//     @Bean
//     public Trigger EntityAmountQuartzTrigger(){ //触发器
//         SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
//                 // .withIntervalInSeconds(30)  //设置时间周期单位秒
//                 .withIntervalInHours(24)  //24小时执行一次
//                 .repeatForever();
//         return TriggerBuilder.newTrigger().forJob(EntityAmountQuartzDetail())
// //                .forJob(quartzDetail_2())
//                 .withIdentity("ENTITY_AMOUNT_INFO_TRIGGER")
//                 .withSchedule(scheduleBuilder)
//                 .build();
//     }
    //endregion

}

