package com.cydeo.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;


@Aspect
@Configuration
public class PerformanceAspect {

    Logger logger = LoggerFactory.getLogger(PerformanceAspect.class);

    @Pointcut("@annotation(com.cydeo.annotation.ExecutionTimeLog)")
    private void anyExecutionTimeOperation() {
    }

    @Around("anyExecutionTimeOperation()")
    public Object anyExecutionTimeOperationAdvice(ProceedingJoinPoint proceedingJoinPoint){

        long beforeTime = System.currentTimeMillis();
        Object result;
       logger.info( "Execution will start");

        try {
            result=proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        long afterTime=System.currentTimeMillis();

          logger.info("Time taken to execute : {} ms - Method: {} - Parameters:{}",(afterTime-beforeTime),proceedingJoinPoint.getSignature().toShortString(),proceedingJoinPoint.getArgs());
        return result;
    }




}
