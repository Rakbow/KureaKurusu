package com.rakbow.kureakurusu.config;

import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.Configuration;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * @author Rakbow
 * @since 2026/3/17 22:52
 */
@org.springframework.context.annotation.Configuration
public class QiniuConfig {

    @Bean
    public Configuration qiniuConfiguration() {
        return Configuration.create(Region.autoRegion());
    }

    /**
     * 构造一个带指定Zone对象的配置类,不同的七云牛存储区域调用不同的zone
     * 华东-浙江2 CnEast2
     */
    @Bean
    public UploadManager uploadManager(Configuration cfg) {
        return new UploadManager(cfg);
    }

    @Bean
    public Auth auth(
            @Value("${qiniu.access-key}") String ak,
            @Value("${qiniu.secret-key}") String sk
    ) {
        return Auth.create(ak, sk);
    }

    @Bean
    public BucketManager bucketManager(Auth auth, Configuration cfg) {
        return new BucketManager(auth, cfg);
    }

}
