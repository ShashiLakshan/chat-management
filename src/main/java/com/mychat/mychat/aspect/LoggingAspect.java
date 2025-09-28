package com.mychat.mychat.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE - 10)
public class LoggingAspect {

    @Around("within(com.mychat.mychat.controller..*)")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest req = currentRequest();
        String userId = header(req, "X-User-Id");

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.getDeclaringType().getSimpleName() + "." + methodSignature.getName();

        log.info("method={}, user={}", methodName, userId);
        return joinPoint.proceed();
    }

    @Around("within(com.mychat.mychat.service..*)")
    public Object logServiceArgs(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest req = currentRequest();
        String userId = header(req, "X-User-Id");

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringType().getSimpleName() + "." + signature.getName();

        log.info("method={}, user={}", methodName, userId);

        return joinPoint.proceed();
    }

    private HttpServletRequest currentRequest() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes instanceof ServletRequestAttributes attrs) return attrs.getRequest();
        return null;
    }

    private String header(HttpServletRequest req, String name) {
        return req == null ? null : req.getHeader(name);
    }

}
