package io.geewit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;


/**
 * @author geewit
 */
@SpringBootApplication(scanBasePackages = {"io.geewit"})
@EntityScan(basePackages = {"io.geewit.**.entity"})
@EnableSpringDataWebSupport
@PropertySource(value = {"classpath:env/${spring.profiles.active}/application.properties"}, encoding = "UTF-8")
@EnableJpaRepositories(basePackages = {"io.geewit.**.dao"})
public class Bootstrap {

    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class, args);
    }

}
