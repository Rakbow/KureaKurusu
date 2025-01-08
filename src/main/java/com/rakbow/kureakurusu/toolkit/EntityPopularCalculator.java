package com.rakbow.kureakurusu.toolkit;

/**
 * @author Rakbow
 * @since 2025/1/8 7:15
 */
public class EntityPopularCalculator {

    private static final double TIME_DECAY_CONSTANT = 0; // 时间衰减系数 通常-0.01 不考虑-0
    private static final double LIKE_WEIGHT = 10.0; // 点赞的权重
    private static final double VISIT_WEIGHT = 3.0; // 点赞的权重

    /**
     * 计算单个内容的热度值
     *
     * @param visit      访问数
     * @param like      点赞数
     * @param createdAt  内容创建时间（以毫秒为单位的时间戳）
     * @return 热度值
     */
    public static double calculateHotness(long visit, long like, long createdAt) {
        // 当前时间
        long currentTime = System.currentTimeMillis();
        // 内容发布后经过的时间（以小时为单位）
        double hoursSinceCreated = (currentTime - createdAt) / (1000.0 * 60 * 60);

        // 计算热度值公式
        double logViews = Math.log1p(visit); // log(1 + visit)
        double baseScore = VISIT_WEIGHT * logViews + LIKE_WEIGHT * like; // 基础分数
        double timeDecay = Math.exp(-TIME_DECAY_CONSTANT * hoursSinceCreated); // 时间衰减因子
        return Math.round(baseScore * timeDecay * 100.0) / 100.0;
    }
}
