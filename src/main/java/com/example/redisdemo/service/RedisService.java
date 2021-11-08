package com.example.redisdemo.service;

public interface RedisService
{
    String getNumberWithCacheable(int number);

    String getNumberWithCacheManager(int number);
}
