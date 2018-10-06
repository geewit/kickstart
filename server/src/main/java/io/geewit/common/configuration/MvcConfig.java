package io.geewit.common.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.geewit.core.jackson.databind.serializer.JsonPageSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author geewit
 * @since 2017-04-07
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    private final static Logger logger = LoggerFactory.getLogger(MvcConfig.class);

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        logger.info("addArgumentResolvers: sortArgumentResolver, pageableArgumentResolver");
        SortHandlerMethodArgumentResolver sortArgumentResolver = new SortHandlerMethodArgumentResolver();
        sortArgumentResolver.setPropertyDelimiter(",");
        PageableHandlerMethodArgumentResolver pageableArgumentResolver = new PageableHandlerMethodArgumentResolver(sortArgumentResolver);
        pageableArgumentResolver.setPageParameterName("page");
        pageableArgumentResolver.setSizeParameterName("size");
        pageableArgumentResolver.setOneIndexedParameters(true);
        argumentResolvers.add(sortArgumentResolver);
        argumentResolvers.add(pageableArgumentResolver);
    }

    //    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/api/**").allowCredentials(true).allowedOrigins("http://localhost", "http://*.geewit.com").allowedMethods("*");
//    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        logger.info("addResourceHandlers: /**");
//        registry.addResourceHandler("/**").addResourceLocations("classpath:resources", "classpath:META-INF/resources", "classpath:BOOT-INF/classes/resources");
//    }

//    @Override
//    public void configurePathMatch(PathMatchConfigurer configurer) {
//        super.configurePathMatch(configurer);
//        configurer.setUseSuffixPatternMatch(false);
//    }

    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        return new Jackson2ObjectMapperBuilder()
                .failOnUnknownProperties(false)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .serializerByType(Page.class, new JsonPageSerializer());
    }

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);
        filter.setAfterMessagePrefix("REQUEST : ");
        return filter;
    }

    @Bean
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
