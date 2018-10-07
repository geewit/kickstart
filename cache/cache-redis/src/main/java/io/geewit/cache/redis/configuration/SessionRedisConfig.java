package io.geewit.cache.redis.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author geewit
 */
@Configuration
@EnableRedisHttpSession(redisNamespace = "rams.${app.name}.${spring.profiles.active}")
public class SessionRedisConfig {
}
