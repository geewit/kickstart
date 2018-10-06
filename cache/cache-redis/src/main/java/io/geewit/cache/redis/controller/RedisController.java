package io.geewit.cache.redis.controller;

import io.geewit.core.Profiles;
import io.geewit.cache.redis.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author geewit
 */
@Profile({Profiles.INTEGRATION})
@RequestMapping(value = {"/api/redis"})
@RestController
public class RedisController {
    private final static Logger logger = LoggerFactory.getLogger(RedisController.class);

    private final RedisService redisService;

    public RedisController(RedisService redisService) {
        this.redisService = redisService;
    }

    /**
     * 清理session
     *
     * @return
     */
    @RequestMapping(name = "清理redis", value = "/delete/{key}", method = RequestMethod.GET)
    public String delete(@PathVariable("key") String key) {
        redisService.delete(key);
        return "清理成功";
    }
}
