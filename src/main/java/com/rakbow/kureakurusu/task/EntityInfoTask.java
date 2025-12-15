package com.rakbow.kureakurusu.task;

/**
 * 每24小时更新一次浏览,点赞和收藏数据到数据库中
 *
 * @author Rakbow
 * @since 2023-02-22 0:36
 */
// @Component
// public class EntityInfoTask extends QuartzJobBean {
//
//     @Override
//     protected void executeInternal(@NotNull JobExecutionContext context) {
//        // 定时任务逻辑
//        // TODO
//        System.out.println("------redis缓存数据获取中------");
//        //获取所有
//        List<String> likeKeys = redisUtil.keys(LikeUtil.PREFIX_LIKE + LikeUtil.SPLIT + "*");
//        List<String> visitKeys = redisUtil.keys(VisitUtil.PREFIX_VISIT + VisitUtil.SPLIT + "*");
//        System.out.println("------redis缓存数据获取完毕------");
//
//        System.out.println("------缓存数据转换中------");
//        List<Visit> visits = statisticPOMapper.keys2Visit(visitKeys);
//        List<Like> likes = statisticPOMapper.keys2Like(likeKeys);
//        System.out.println("------缓存数据转换完毕------");
//
//        System.out.println("------正在更新至数据库中------");
//        visits.forEach(visit -> {
//            statisticMapper.updateVisitCount(visit.getEntityType(), visit.getEntityId(), visit.getVisitCount());
//        });
//        likes.forEach(like -> {
//            statisticMapper.updateLikeCount(like.getEntityType(), like.getEntityId(), like.getLikeCount());
//        });
//        System.out.println("------实体浏览点赞数据更新完毕------");
//     }

// }
