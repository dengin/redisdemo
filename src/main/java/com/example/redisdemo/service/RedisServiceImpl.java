package com.example.redisdemo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Repository("redisService")
public class RedisServiceImpl implements RedisService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisServiceImpl.class);

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private RedisTemplate redisTemplate;

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

    @Override
    public void saveNumbersUsingTransaction(List<Integer> numbers)
    {
        List<Object> txResults = (List<Object>) redisTemplate.execute(new SessionCallback()
        {
            public List<Object> execute(RedisOperations operations) throws DataAccessException
            {
                operations.multi();
                try
                {
                    StringBuilder redisValue = new StringBuilder("Saved numbers");
                    for (Integer number : numbers)
                    {
                        LOGGER.info("Processed number is " + number);
                        if (number.intValue() == 5)
                        {
                            throw new IllegalArgumentException("Bad number");
                        }
                        redisValue.append("___" + number);
                        operations.opsForValue().set("numbers", redisValue.toString());
                    }
                }
                catch (Exception e)
                {
                    LOGGER.error("Exception occurred: " + e.getMessage());
                    operations.discard();
                    return null;
                }
                return operations.exec();
            }
        });
        if (txResults != null)
        {
            LOGGER.info("Redis process count: " + txResults.size());
        }
    }

    @Override
    @Transactional
    public void saveNumbersUsingTransactionWithAnnotation(List<Integer> numbers)
    {
        redisTemplate.execute(new SessionCallback()
        {
            public List<Object> execute(RedisOperations operations) throws DataAccessException
            {
                operations.multi();

                StringBuilder redisValue = new StringBuilder("Saved numbers");
                for (Integer number : numbers)
                {
                    LOGGER.info("Processed number is " + number);

                    redisValue.append("___" + getNumberAsText(number));
                    operations.opsForValue().set("numbersAsText", redisValue.toString());
                }
                return null;
            }
        });
    }

    private String getNumberAsText(Integer number)
    {
        switch (number)
        {
            case 0:
                return "zero";
            case 1:
                return "one";
            case 2:
                return "two";
            case 3:
                return "three";
            case 4:
                return "four";
            case 5:
                throw new IllegalArgumentException("Bad number");
            default:
                return null;
        }
    }
}
