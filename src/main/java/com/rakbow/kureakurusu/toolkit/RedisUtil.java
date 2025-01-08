package com.rakbow.kureakurusu.toolkit;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 *
 * @author Rakbow
 * @since 2023-01-09 13:13
 */
@Component
@RequiredArgsConstructor
public class RedisUtil {

    private static final Logger log = LoggerFactory.getLogger(RedisUtil.class);
    private final RedisTemplate<String, Object> template;

    //region common

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     */
    public void expire(String key, long time) {
        if (time > 0) template.expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0 代表永久有效
     */
    public long getExpire(String key) {
        Long result = template.getExpire(key, TimeUnit.SECONDS);
        return result != null ? result : 0L;
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        if (!StringUtils.hasLength(key)) return false;
        return Boolean.TRUE.equals(template.hasKey(key));
    }

    /**
     * 根据删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void delete(String... key) {
        if (key != null && key.length > 0)
            if (key.length == 1)
                template.delete(key[0]);
            else
                template.delete((Collection<String>) CollectionUtils.arrayToList(key));
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            template.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : template.opsForValue().get(key);
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                template.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     */
    public long increment(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        Long result = template.opsForValue().increment(key, delta);
        return result != null ? result : 0L;
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     */
    public long decrement(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        Long result = template.opsForValue().decrement(key, delta);
        return result != null ? result : 0L;
    }

    @SuppressWarnings({"deprecation"})
    public List<String> searchTokenFirst(String key) {
        List<String> res = new ArrayList<>();
        //execute()：搜索 Redis 中某个 key
        Cursor<byte[]> cursor =
                template.execute((RedisCallback<Cursor<byte[]>>) connection ->
                        connection.scan(ScanOptions.scanOptions().match(STR."*\{key}*").build()));
        //将 redis scan 迭代的结果的第一条转为字符串
        while (true) {
            assert cursor != null;
            if (!cursor.hasNext()) break;
            res.add(new String(cursor.next()));
        }
        return res;
    }


    //endregion

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> t) {
        Object data = template.opsForValue().get(key);
        if (null == data) {
            return null;
        }
        if (data.getClass().equals(t)) {
            return (T) data;
        } else if (data instanceof LinkedHashMap) {
            return JsonUtil.to(data.toString(), t);
        } else if (data instanceof String) {
            return JsonUtil.to((String) data, t);
        } else {
            throw new Exception("redis 数据错误，无法完成转换");
        }
    }

    /**
     * 更新 Redis ZSET 数据
     *
     * @param key   排行榜 key
     * @param id    数据的唯一标识
     * @param score 热度值
     */
    public void updateZSet(String key, long id, double score) {
        // 更新 ZSET 的分值
        template.opsForZSet().add(key, id, score);

        // 删除超出前10名的数据
        long rankSize = template.opsForZSet().zCard(key); // 获取 ZSET 的元素数量
        if (rankSize > 10) {
            template.opsForZSet().removeRange(key, 0, rankSize - 11); // 删除第 11 名及之后的数据
        }
    }

    public Set<Object> getZSet(String key, int topN) {
        // 更新 ZSET 的分值
        return template.opsForZSet().reverseRange(key, 0, topN - 1);
    }
}

