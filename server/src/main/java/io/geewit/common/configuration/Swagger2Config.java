package io.geewit.common.configuration;

import io.geewit.core.Profiles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author geewit
 */
@Profile({Profiles.DEVELOPMENT, Profiles.INTEGRATION, Profiles.PREVIEW})
@Configuration
@EnableSwagger2
public class Swagger2Config {

    private final Environment environment;

    public Swagger2Config(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public Docket createApiDoc() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("apiGroup")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("io.geewit"))
                .paths(PathSelectors.ant("/api/**"))
                .build();
    }


    @Bean
    public Docket createWsDoc() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("wsGroup")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("io.geewit"))
                .paths(PathSelectors.ant("/ws/**"))
                .build();
    }


    private ApiInfo apiInfo() {
        String version = environment.getProperty("spring.application.version", "版本号:1.0.0");
        return new ApiInfoBuilder()
                .title("Spring Boot中使用Swagger2构建RESTful APIs")
                .description(environment.getProperty("spring.application.name", "spring boot application"))
                .termsOfServiceUrl("http://www.geewit.io")
                .contact(new Contact("黄方", "http://www.geewit.io", "hf@geewit.io"))
                .version(version)
                .build();
    }

}
