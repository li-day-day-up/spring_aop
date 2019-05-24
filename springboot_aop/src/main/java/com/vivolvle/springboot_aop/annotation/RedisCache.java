package com.vivolvle.springboot_aop.annotation;

import java.lang.annotation.*;

/**
 * @author weilz
 * @date 2019/5/24
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisCache {
    Class type();
}
