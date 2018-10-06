package io.geewit.cache.redis.service;

import io.geewit.core.Profiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author geewit
 */
@Profile({Profiles.INTEGRATION})
@Service
public class RedisService {
    private final static Logger logger = LoggerFactory.getLogger(RedisService.class);

    private final StringRedisTemplate redisTemplate;

    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
