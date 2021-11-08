package com.example.redisdemo;

import com.example.redisdemo.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Random;

@SpringBootApplication
@EnableCaching
public class RedisDemoApplication implements CommandLineRunner
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisDemoApplication.class);

    @Autowired
    @Qualifier("redisService")
    private RedisService redisService;

    @Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args)
    {
        SpringApplication.run(RedisDemoApplication.class, args);
    }

    @Override
    public void run(String... args) {

        RedisTemplate redisTemplate = (RedisTemplate) applicationContext.getBean("redisTemplate");

        ValueOperations valueOperations = redisTemplate.opsForValue();
        String key = "test_key_redisTemplate";
        valueOperations.set(key, "test_value");

        LOGGER.info("RediDemoApplication is started and the key '{}' is stored in Redis Server", key);

        //getNumberWithCacheable();
        getNumberWithCacheManager();
    }

    private void getNumberWithCacheable()
    {
        Random r = new Random();
        for (int i = 0; i < 10; i++)
        {
            int n = r.nextInt(5);

            LOGGER.info("Method: getNumberWithCacheable is called with parameter: " + n);
            LOGGER.info("Result: " + redisService.getNumberWithCacheable(n));
        }
    }

    private void getNumberWithCacheManager()
    {
        Random r = new Random();
        for (int i = 0; i < 10; i++)
        {
            int n = r.nextInt(5);

            LOGGER.info("Method: getNumberWithCacheManager is called with parameter: " + n);
            LOGGER.info("Result: " + redisService.getNumberWithCacheManager(n));
        }
    }
}
