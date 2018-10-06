package io.geewit.cache.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.lang.reflect.Method;


/**
 * @author geewit
 * @since 2017-04-07
 */
@Configuration
public class CacheConfig {
    private final static Logger logger = LoggerFactory.getLogger(CacheConfig.class);


    private static String activeProfile;

    public CacheConfig(Environment environment) {
        activeProfile = environment.getActiveProfiles()[0];
    }

    private static String generate(Object target, Method method, Object... params) {
        StringBuilder sb = new StringBuilder(activeProfile);
        sb.append('.');
        sb.append(target.getClass().getSimpleName());
        sb.append('.');
        sb.append(method.getName());
        for (Object param : params) {
            sb.append('.');
            sb.append(param.toString());
        }
        return sb.toString();
    }


    @Bean
    public KeyGenerator wiselyKeyGenerator(){
        return CacheConfig::generate;
    }
}
