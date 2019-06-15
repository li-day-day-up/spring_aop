package com.vivolvle.redis_cache.service;

import com.vivolvle.redis_cache.entity.MapTheme;

/**
 * @Author: weilz
 * @Date: 2019/6/14 17:50
 */
public interface MapThemeService {
    MapTheme getById(Long id);

    void update(Long id, String name);
}
