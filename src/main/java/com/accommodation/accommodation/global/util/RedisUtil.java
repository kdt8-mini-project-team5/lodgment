package com.accommodation.accommodation.global.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate redisTemplate;

    public void setValue(String key, Object value, Long timeUnit, TimeUnit time) {
        redisTemplate.opsForValue().set(key, value, timeUnit, time);
    }

    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public Optional<Object> getValue(String key) {
        return Optional.of(redisTemplate.opsForValue().get(key));
    }

}