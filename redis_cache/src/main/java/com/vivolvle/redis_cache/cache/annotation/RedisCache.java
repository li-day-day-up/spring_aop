package com.vivolvle.redis_cache.cache.annotation;

import java.lang.annotation.*;

/**
 * @Author: weilz
 * @Date: 2019/6/14 17:51
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisCache {

    /**
     * redisKey
     *
     * @return
     */
    Class type();
}
