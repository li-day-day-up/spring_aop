package com.vivolvle.redis_cache.service.impl;

import com.vivolvle.redis_cache.cache.annotation.RedisCache;
import com.vivolvle.redis_cache.entity.MapTheme;
import com.vivolvle.redis_cache.repository.MapThemeRepository;
import com.vivolvle.redis_cache.service.MapThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: weilz
 * @Date: 2019/6/14 17:50
 */
@Service
public class MapThemeServiceImpl implements MapThemeService {
    @Autowired
    private MapThemeRepository mapThemeRepository;

    @Override
    @RedisCache(type = MapTheme.class)
    public MapTheme getById(Long id) {
        return mapThemeRepository.findById(id).get();
    }
}
