package com.lingyuecm.aspect;

import com.lingyuecm.common.PageData;
import com.lingyuecm.utils.ContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * The aspect for caching the pagination data
 */
@Slf4j
@Aspect
@Component
public class PaginationAspect {
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController) && args(.., com.lingyuecm.common.PageData)")
    public void pointcut() {
        // Handler methods whose last parameter is of type PageData
    }

    @Before("pointcut()")
    public void beforePagination(JoinPoint joinPoint) {
        log.info("Pagination detected, caching pagination data");
        Object[] arguments = joinPoint.getArgs();
        /*
        The cached data doesn't need clearing in @AfterReturning or @AfterThrowing notification,
        since all cached data will be cleared on completing the request in TokenInterceptor.java
        or on exceptions in LcmExceptionHandler.java
         */
        ContextUtils.setPageData((PageData) arguments[arguments.length - 1]);
        log.info("Pagination data cached");
    }
}
