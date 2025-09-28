package com.mychat.mychat.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE - 10)
public class LoggingAspect {

    @Around("within(com.mychat.mychat.controller..*)")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.getDeclaringType().getSimpleName() + "." + methodSignature.getName();

        log.info("method={}", methodName);
        return joinPoint.proceed();
    }

    @Around("within(com.mychat.mychat.service..*)")
    public Object logServiceArgs(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringType().getSimpleName() + "." + signature.getName();

        log.info("method={}", methodName);

        return joinPoint.proceed();
    }

}
