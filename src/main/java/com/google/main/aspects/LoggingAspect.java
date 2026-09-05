package com.google.main.aspects;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    private final HttpServletRequest request;

    public LoggingAspect(HttpServletRequest request) {
        this.request = request;
    }

    // @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    @Around("execution(* com.google.main.controller.*.*(..))")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        String controller = joinPoint.getTarget().getClass().getSimpleName();

        String method = joinPoint.getSignature().getName();

        String httpMethod = request.getMethod();
        String requestURI = request.getRequestURI();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        log.info("[Controller] {} {} -> {}.{}() executed in {} ms",
                httpMethod, requestURI, controller, method, (endTime - startTime));
        return result;
    }

    @Around("execution(* com.google.main.service.*.*(..))")
    public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        String service = joinPoint.getTarget().getClass().getSimpleName();

        String method = joinPoint.getSignature().getName();

        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        log.info("[Service] -> {}.{}() executed in {} ms",
                service, method, (endTime - startTime));
        return result;
    }

}
