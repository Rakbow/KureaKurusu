package com.rakbow.kureakurusu.toolkit;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Rakbow
 * @since 2025/12/29 20:39
 */
@RequiredArgsConstructor
@Component
public class MybatisBatchUtil {

    private final SqlSessionFactory sqlFactory;

    @SneakyThrows
    public <T> void batchInsert(List<T> data, Class<? extends BaseMapper<T>> clazz) {
        if (CollectionUtil.isEmpty(data)) return;
        MybatisBatch.Method<T> method = new MybatisBatch.Method<>(clazz);
        MybatisBatch<T> batch = new MybatisBatch<>(sqlFactory, data);
        batch.execute(method.insert());
    }

    @SneakyThrows
    public <T> void batchUpdateById(List<T> data, Class<? extends BaseMapper<T>> clazz) {
        if (CollectionUtil.isEmpty(data)) return;
        MybatisBatch.Method<T> method = new MybatisBatch.Method<>(clazz);
        MybatisBatch<T> batch = new MybatisBatch<>(sqlFactory, data);
        batch.execute(method.updateById());
    }
}
