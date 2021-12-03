package com.example.redisdemo.service;

import java.util.List;

public interface RedisService
{
    String getNumberWithCacheable(int number);

    String getNumberWithCacheManager(int number);

    void saveNumbersUsingTransaction(List<Integer> numbers);

    void saveNumbersUsingTransactionWithAnnotation(List<Integer> numbers);
}
