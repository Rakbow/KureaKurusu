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
    @Around("within(@org.springframework.stereotype.Service *) && execution(* *(..))")
    public Object setSearchTime(ProceedingJoinPoint pjp) {
        long start = System.currentTimeMillis();
        Object result = pjp.proceed();
        String time = String.format("%.2f", (System.currentTimeMillis() - start) / 1000.0);
        if (result instanceof SearchResult<?> searchResult) searchResult.time = time;
        return result;
    }

}
