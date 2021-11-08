package com.example.redisdemo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix="redis")
@Data
public class RedisConfigurationProperties
{
    private long defaultTimeout;
    private int port;
    private String host;
    Map<String, Long> expirations = new HashMap<>();
}
