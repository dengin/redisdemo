package com.example.redisdemo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository("redisService")
public class RedisServiceImpl implements RedisService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisServiceImpl.class);

    @Autowired
    private CacheManager cacheManager;

    @Override
    @Cacheable(value = "numberCache")
    public String getNumberWithCacheable(int number)
    {
        LOGGER.info("Method (getNumberWithCacheable) is processed");
        return "Number: " + number;
    }

    @Override
    public String getNumberWithCacheManager(int number)
    {
        LOGGER.info("Method (getNumberWithCacheManager) is processed");
        Cache cache = Objects.requireNonNull(cacheManager.getCache("numberCache"));
        String cachedValue = cache.get(number, String.class);
        if (cachedValue == null)
        {
            cache.put(number, ("Number: " + number));
            LOGGER.info("Number is cached with cacheManager");
            cachedValue = cache.get(number, String.class);
        }
        return cachedValue;
    }
}
