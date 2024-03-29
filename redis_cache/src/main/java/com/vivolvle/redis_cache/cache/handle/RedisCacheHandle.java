package com.vivolvle.redis_cache.cache.handle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.vivolvle.redis_cache.cache.annotation.RedisCache;
import com.vivolvle.redis_cache.cache.annotation.RedisEvict;
import com.vivolvle.redis_cache.utils.Base64Util;
import com.vivolvle.redis_cache.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: weilz
 * @Date: 2019/6/14 17:51
 */
@Aspect
@Component
public class RedisCacheHandle {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * RedisCache处理
     *
     * @param point
     * @return java.lang.Object
     */
    @Around("execution(public * * (..) ) && @annotation(com.vivolvle.redis_cache.cache.annotation.RedisCache)")
    public Object handle(ProceedingJoinPoint point) throws Throwable {

        // 得到类名、方法名和参数
        String clazzName = point.getTarget().getClass().getName();
        String methodName = point.getSignature().getName();
        Object[] args = point.getArgs();
        Method me = ((MethodSignature) point.getSignature()).getMethod();
        Class modelType = me.getAnnotation(RedisCache.class).type();

        // 根据类名，方法名和参数生成key
        String key = genKey(clazzName, methodName, args);
        // 检查redis中是否有缓存
        String value = (String) redisUtil.getMapKey(modelType.getName(), key);
        Object result = null;
        if (StringUtils.isBlank(value)) {
            // 调用方法
            result = point.proceed(args);

            String json = serialize(result);
            redisUtil.setMap(modelType.getName(), key, json);
        } else {
            // 得到被代理方法的返回值类型
            Class returnType = ((MethodSignature) point.getSignature()).getReturnType();
            Boolean isList = returnType.isAssignableFrom(List.class);
            if (isList) {
                Type temp = me.getGenericReturnType();
                Type[] types = ((ParameterizedType) temp).getActualTypeArguments();
                Class real = (Class) types[0];
                result = deserialize(Base64Util.base2String(value), isList, real);
            } else {
                result = deserialize(Base64Util.base2String(value), isList, returnType);
            }
        }
        return result;
    }

    /**
     * RedisEvict处理
     *
     * @param jp
     * @return java.lang.Object
     */
    @Around("execution(public * * (..) ) && @annotation(com.vivolvle.redis_cache.cache.annotation.RedisEvict)")
    public Object evictCache(ProceedingJoinPoint jp) throws Throwable {

        // 得到被代理的类名，方法
        String clazzName = jp.getTarget().getClass().getName();
        Method me = ((MethodSignature) jp.getSignature()).getMethod();
        Signature signature = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        RedisEvict redisEvict = me.getAnnotation(RedisEvict.class);

        Class modelType = redisEvict.type();
        String methodName = redisEvict.methodName();
        String parameterNames = redisEvict.parameterName();
        // 清除对应缓存
        if (StringUtils.isBlank(methodName) || StringUtils.isBlank(parameterNames)) {
            //清除整个map
            redisUtil.removeKey(modelType.getName());
        } else {
            //清除map中单个key
            //获取所有参数名
            String[] allParameterNames = methodSignature.getParameterNames();
            List<String> allPara = Lists.newArrayList(allParameterNames);
            //获取需要的参数名
            String[] parameterNameArray = parameterNames.split(",");
            //获取结果
            List<Object> argsList = Lists.newArrayList();
            for (String parameterName : parameterNameArray) {
                argsList.add(jp.getArgs()[allPara.indexOf(parameterName)]);
            }
            Object[] args = new Object[parameterNameArray.length];
            argsList.toArray(args);

            String mapKey = genKey(clazzName, methodName, args);
            redisUtil.deleteMapKey(modelType.getName(), mapKey);
        }
        return jp.proceed(jp.getArgs());
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

    protected String serialize(Object target) throws JsonProcessingException {
        return Base64Util.string2Base(objectMapper.writeValueAsString(target));
    }

    protected Object deserialize(String jsonString, Boolean isList, Class real) throws IOException {
        // 序列化结果是List对象
        if (isList) {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, real);
            return objectMapper.readValue(jsonString, javaType);
        }
        // 序列化结果是普通对象
        return objectMapper.readValue(jsonString, real);
    }
}
