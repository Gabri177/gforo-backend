package com.yugao.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class ControllerLogAspect {

    @Pointcut("execution(* com.yugao.controller..*(..))")
    public void controllerMethods(){}

    @Around("controllerMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
//        Object[] args = joinPoint.getArgs();

//        log.info("Entering method: {}.{}()", className, methodName);
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
//        log.info("Exiting method: {}.{}(). Execution time: {} ms",
//                className, methodName, (endTime - startTime));
        return result;
    }
}
