package com.hardwareaplications.hardware.AOP;

import com.hardwareaplications.hardware.Exception.ProductNotFoundException;
import com.hardwareaplications.hardware.Exception.ProductValidationException;
import jakarta.validation.ConstraintViolationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Component
@Aspect
public class PerfoMonAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(PerfoMonAspect.class);
    @Around("execution(* com.hardwareaplications.hardware..*(..))")
    public Object logMethodExecutionTime(ProceedingJoinPoint jp) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object obj = jp.proceed();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        LOGGER.info("Method executed in " + duration + " milliseconds");
        return obj;
    }
}