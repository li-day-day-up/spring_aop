package com.vivolvle.springboot_aop.controller;

import com.vivolvle.springboot_aop.annotation.RedisCache;
import com.vivolvle.springboot_aop.entity.Hello;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author weilz
 * @date 2019/5/24
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    @RedisCache(type = Hello.class)
    public void hello(@RequestParam("id") Integer id) {
        System.out.println("hello world" + id);
    }
}
