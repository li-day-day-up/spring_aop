package com.vivolvle.redis_cache.controller;

import com.vivolvle.redis_cache.entity.MapTheme;
import com.vivolvle.redis_cache.service.MapThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: weilz
 * @Date: 2019/6/14 17:51
 */
@RestController
public class MapThemeController {
    @Autowired
    private MapThemeService mapThemeService;

    @GetMapping("/getById")
    public MapTheme getById(@RequestParam("id") Long id) {
        return mapThemeService.getById(id);
    }
}
