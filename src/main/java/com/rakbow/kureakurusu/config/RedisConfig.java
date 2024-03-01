package com.rakbow.kureakurusu.config;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring.data.redis.FastJsonRedisSerializer;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.UnifiedJedis;

import java.time.Duration;

/**
 * @author Rakbow
 * @since 2023-01-09 13:13 对Redis进行配置
 */

@RequiredArgsConstructor
@Configuration
@EnableCaching
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;
    @Value("${spring.data.redis.password}")
    private String password;
    @Value("${spring.data.redis.database}")
    private int database;
    @Value("${spring.data.redis.timeout}")
    private int timeout;

    private final ApplicationContext mApplicationContext;
    private final RedisProperty mRedisProperty;
    private final RedisConnectionFactory mRedisConnectionFactory;

    @Bean
    public GenericObjectPoolConfig<Jedis> jedisPoolConfig() {
        GenericObjectPoolConfig<Jedis> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(10);
        config.setMaxIdle(5);
        return config;
    }

    @Bean
    @SuppressWarnings("unchecked")
    public UnifiedJedis unifiedJedis(GenericObjectPoolConfig jedisPoolConfig) {
        UnifiedJedis client;
        if (StringUtils.isNotEmpty(password)) {
            client = new JedisPooled(jedisPoolConfig, host, port, timeout, password, database);
        } else {
            client = new JedisPooled(jedisPoolConfig, host, port, timeout, null, database);
        }
        return client;
    }

    FastJsonConfig getRedisFastJson(){
        FastJsonConfig config = new FastJsonConfig();
        config.setWriterFeatures(
                // 保留 Map 空的字段
                JSONWriter.Feature.WriteMapNullValue,

                JSONWriter.Feature.WriteNullListAsEmpty,
                // 写入类名
                JSONWriter.Feature.WriteClassName,
                // 将 Boolean 类型的 null 转成 false
                JSONWriter.Feature.WriteNullBooleanAsFalse,
                JSONWriter.Feature.WriteEnumsUsingName);
        config.setReaderFeatures(
                JSONReader.Feature.SupportClassForName,
                // 支持autoType
                JSONReader.Feature.SupportAutoType);
        return config;
    }

    @Bean
    @SuppressWarnings("unchecked")
    FastJsonRedisSerializer getFastJsonRedisSerializer() {
        FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer(Object.class);
        fastJsonRedisSerializer.setFastJsonConfig(getRedisFastJson());
        return fastJsonRedisSerializer;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory, FastJsonRedisSerializer pFastJsonRedisSerializer) {

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(mRedisProperty.getCacheExpire()))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(pFastJsonRedisSerializer));
        return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(mRedisConnectionFactory))
                .cacheDefaults(config)
                .build();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory, FastJsonRedisSerializer pFastJsonRedisSerializer) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setEnableTransactionSupport(true);
        //序列化设置 ，这样计算是正常显示的数据，也能正常存储和获取
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(pFastJsonRedisSerializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(pFastJsonRedisSerializer);
        return redisTemplate;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(
            RedisConnectionFactory factory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(factory);
        return stringRedisTemplate;
    }
}

