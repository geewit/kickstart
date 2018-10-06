package io.geewit.cache.redis.configuration;

import io.geewit.core.Profiles;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author geewit
 */
@Profile({Profiles.INTEGRATION, Profiles.PREVIEW, Profiles.PRODUCTION})
@Configuration
@EnableRedisHttpSession(redisNamespace = "rams.${app.name}.${spring.profiles.active}")
public class SessionRedisConfig {
}
