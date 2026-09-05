//package com.google.main.interceptors;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//@Component
//public class ApiLoggingInterceptor implements HandlerInterceptor {
//
//    private static final Logger log = LoggerFactory.getLogger(ApiLoggingInterceptor.class);
//
//    @Override
//    public boolean preHandle(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            Object handler
//    ) {
//        if (handler instanceof HandlerMethod handlerMethod) {
//            log.info(
//                    "API_REQUEST method={} path={} controller={} method={} queryParams={}",
//                    request.getMethod(),
//                    request.getRequestURI(),
//                    handlerMethod.getBeanType().getSimpleName(),
//                    handlerMethod.getMethod().getName(),
//                    request.getQueryString()
//            );
//        }
//
//        return true;
//    }
//
//}
