package com.vivolvle.springboot_aop.handle;

import com.vivolvle.springboot_aop.annotation.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author weilz
 * @date 2019/5/24
 */
@Aspect
@Component
@Slf4j
public class RedisCacheHandle {

    @Around("@annotation(com.vivolvle.springboot_aop.annotation.RedisCache)")
    public void handle(ProceedingJoinPoint point) throws Throwable {
        // 得到类名、方法名和参数
        String clazzName = point.getTarget().getClass().getName();
        String methodName = point.getSignature().getName();
        Object[] args = point.getArgs();
        Method me = ((MethodSignature) point.getSignature()).getMethod();
        Class modelType = me.getAnnotation(RedisCache.class).type();
        // 根据类名，方法名和参数生成key
        String key = genKey(clazzName, methodName, args);

        log.info("this is before");
        point.proceed();
        log.info("this is after");
    }

    /**
     * 生成唯一key
     *
     * @param clazzName
     * @param methodName
     * @param args
     * @return java.lang.String
     */
    protected String genKey(String clazzName, String methodName, Object[] args) {
        StringBuilder sb = new StringBuilder(clazzName);
        sb.append(".");
        sb.append(methodName);
        sb.append(".");
        for (Object obj : args) {
            sb.append(obj.toString());
            sb.append(".");
        }
        return sb.toString();
    }

}
