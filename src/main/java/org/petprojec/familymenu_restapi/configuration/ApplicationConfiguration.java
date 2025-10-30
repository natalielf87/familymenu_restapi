package org.petprojec.familymenu_restapi.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "org.petprojec.familymenu_restapi.repositories")
public class ApplicationConfiguration {

}
