package com.yeonseong.board.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class TimeTrace {

  @Around("execution(* com.yeonseong.board.controller..*(..))")
  public Object execute(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    long start = System.currentTimeMillis();
    log.info("START: {}", proceedingJoinPoint.toLongString());
    try {
      return proceedingJoinPoint.proceed();
    } finally {
      long finish = System.currentTimeMillis();
      long timeMs = finish - start;
      log.info("END: {} -> {}ms", proceedingJoinPoint.toLongString(), timeMs);
    }
  }
}
