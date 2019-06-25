package com.vivolvle.redis_cache.cache.annotation;

import java.lang.annotation.*;

/**
 * @Author: weilz
 * @Date: 2019/6/14 17:51
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisEvict {

    /**
     * redisKey
     *
     * @param
     * @return java.lang.Class
     */
    Class type();

    /**
     * methodName
     *
     * @param
     * @return java.lang.String
     */
    String methodName() default "";

    /**
     * parameterName
     *
     * @param
     * @return java.lang.String
     */
    String parameterName() default "";
}
