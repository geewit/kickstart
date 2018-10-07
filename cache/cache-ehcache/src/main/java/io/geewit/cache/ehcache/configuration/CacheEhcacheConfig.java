package io.geewit.cache.ehcache.configuration;

import net.sf.ehcache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheManagerUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;


/**
 * @author geewit
 * @since 2017-04-07
 */
@EnableCaching
@Configuration
public class CacheEhcacheConfig {
    private final static Logger logger = LoggerFactory.getLogger(CacheEhcacheConfig.class);

    @Bean
    @ConditionalOnMissingBean
    public CacheManager ehCacheCacheManager() {
        return EhCacheManagerUtils.buildCacheManager(new ClassPathResource("ehcache.xml"));
    }

}
