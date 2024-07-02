package com.accommodation.accommodation.global.model.repository;

import java.time.Duration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisLockRepository {

    private RedisTemplate<String, String> redisTemplate;


    public RedisLockRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Boolean lock(Long key) {
        return redisTemplate
            .opsForValue()
            .setIfAbsent(gerateKey(key), "lock", Duration.ofMillis(3_000));
    }

    public Boolean unlock(Long key) {
        return redisTemplate.delete(gerateKey(key));
    }

    private String gerateKey(Long key) {
        return key.toString();
    }

}
