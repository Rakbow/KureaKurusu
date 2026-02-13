package com.rakbow.kureakurusu.aspect;

import com.rakbow.kureakurusu.data.SearchResult;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author Rakbow
 * @since 2025/10/24 0:07
 */
@Slf4j
@Aspect
@Component
public class SearchAspect {

    /**
     * set search time to all search method
     */
    @SneakyThrows
    @Around("@annotation(com.rakbow.kureakurusu.annotation.Search)")
    public Object recordSearchTime(ProceedingJoinPoint pjp) {
        long start = System.nanoTime();
        Object result = pjp.proceed();
        if (result instanceof SearchResult<?> searchResult)
            searchResult.time = String.format("%.3f", (System.nanoTime() - start) / 1000_000_000.000);
        return result;
    }

}
