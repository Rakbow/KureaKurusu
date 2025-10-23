package com.rakbow.kureakurusu.toolkit;

import org.apache.commons.math3.util.FastMath;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Rakbow
 * @since 2025/1/8 7:15
 */
@Component
public class HotnessCalculator {

    //item
    private static final double ITEM_TIME_DECAY_CONSTANT = -0.01; // 时间衰减系数 通常-0.01 不考虑-0
    private static final double ITEM_LIKE_WEIGHT = 10.0; // 点赞的权重
    private static final double ITEM_VISIT_WEIGHT = 3.0; // 点赞的权重

    // 放大因子（解决小数精度问题）
    private static final int SCALING_FACTOR = 10000;

    //entry
    @Value("${hotness.weights.view}") private double viewWeight;
    @Value("${hotness.weights.like}") private double likeWeight;
    @Value("${hotness.weights.product}") private double itemCountWeight;

    @Value("${hotness.product.lowThreshold}") private int lowBaseThreshold;
    @Value("${hotness.product.midThreshold}") private int midBaseThreshold;
    @Value("${hotness.product.highThreshold}") private int highBaseThreshold;

    @Value("${hotness.scale.like}") private double likeScale;
    @Value("${hotness.scale.view}") private int viewScale;

    /**
     * 计算单个内容的热度值
     *
     * @param visit     访问数
     * @param like      点赞数
     * @param createdAt 内容创建时间（以毫秒为单位的时间戳）
     * @return 热度值
     */
    public double calculateItemHotness(long visit, long like, long createdAt) {
        // 当前时间
        long currentTime = System.currentTimeMillis();
        // 内容发布后经过的时间（以小时为单位）
        double hoursSinceCreated = (createdAt - createdAt) / (1000.0 * 60 * 60);

        // 计算热度值公式
        double logViews = Math.log1p(visit); // log(1 + visit)
        double baseScore = ITEM_VISIT_WEIGHT * logViews + ITEM_LIKE_WEIGHT * like; // 基础分数
        double timeDecay = Math.exp(-ITEM_TIME_DECAY_CONSTANT * hoursSinceCreated); // 时间衰减因子
        return Math.round(baseScore * timeDecay * 100.0) / 100.0;
    }

    /**
     * 核心热度计算方法
     * @return 计算后的热度值
     */
    public double calculateEntryHotness(long visit, long like, int itemCount) {
        // 1. 浏览数处理（对数变换防止极端值主导）
        double viewScore = viewWeight * normalizeViews((int) visit);
        // 2. 点赞数处理（Sigmoid函数处理边际效应）
        double likeScore = likeWeight * normalizeLikes((int) like);
        // 3. 关联商品数处理（三阶段函数适应大跨度范围）
        double itemCountScore = itemCountWeight * itemCountScaleFunction(itemCount);
        // 4. 防垄断机制：确保商品数贡献不超过总分的50%
        // if (itemCountScore > 0.5 * (viewScore + likeScore)) {
        //     itemCountScore = 0.5 * (viewScore + likeScore);
        // }
        double rawHotness = viewScore + likeScore + itemCountScore;
        return Math.round(rawHotness * SCALING_FACTOR);
    }

    /**
     * 关联物品数三阶段处理函数
     * 个位数(0-30): 线性增长 -> 小微企业保护
     * 中基数(30-800): 对数增长 -> 中型企业合理体现
     * 高基数(800+): 平方根饱和 -> 防止大型企业垄断
     */
    private double itemCountScaleFunction(int itemCount) {
        if (itemCount <= lowBaseThreshold) {
            // 低基数：线性快速提升 (0-10个商品)
            return (double) itemCount / lowBaseThreshold;
        } else if (itemCount <= midBaseThreshold) {
            // 中基数：指数增长 (10-300个商品)
            double normalized = (itemCount - lowBaseThreshold) /
                    (double)(midBaseThreshold - lowBaseThreshold);
            return 0.3 + 0.7 * normalized * normalized; // 平方加速
        } else {
            // 高基数：线性饱和 (300-2000个商品)
            double normalized = Math.min(
                    (itemCount - midBaseThreshold) /
                            (double)(highBaseThreshold - midBaseThreshold),
                    1.0
            );
            return 0.9 + 0.1 * normalized; // 接近饱和
        }
    }

    /**
     * 小数据优化：阶梯式浏览归一化
     * 解决小浏览量导致的分值相同问题
     */
    private double normalizeViews(int viewCount) {
        // 小浏览量阶梯处理（每个浏览都有明显区分）
        if (viewCount <= 50) {
            // 50次浏览内，每个浏览增加0.02分
            return viewCount * 0.02;
        }
        // 中等浏览量：对数处理
        return FastMath.log1p(viewCount) / FastMath.log1p(viewScale);
    }

    /**
     * 小数据优化：离散式点赞归一化
     * 解决小点赞数导致的分值相同问题
     */
    private double normalizeLikes(int likeCount) {
        // 小点赞数离散处理（每个点赞都有明显区分）
        if (likeCount <= 20) {
            // 基础分 + 每个点赞的增量
            return 0.1 + (likeCount * 0.04);
        }
        // 中等点赞数：Sigmoid处理
        return 2 / (1 + FastMath.exp(-likeCount / likeScale)) - 1;
    }

    // ================== 高级功能 ================== //

    /**
     * 动态参数调整（根据企业规模自动配置）
     */
    // public void configureForEnterpriseScale(EnterpriseScale scale) {
    //     switch (scale) {
    //         case SMALL:
    //             lowBaseThreshold = 20;
    //             midBaseThreshold = 300;
    //             highBaseThreshold = 1500;
    //             itemCountWeight = 2.5; // 小微企业的商品权重更高
    //             break;
    //         case MEDIUM:
    //             lowBaseThreshold = 50;
    //             midBaseThreshold = 800;
    //             highBaseThreshold = 5000;
    //             break;
    //         case LARGE:
    //             lowBaseThreshold = 100;
    //             midBaseThreshold = 2000;
    //             highBaseThreshold = 10000;
    //             itemCountWeight = 1.8; // 大型企业商品权重降低
    //             break;
    //     }
    // }
}
