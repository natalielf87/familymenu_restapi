package org.petprojec.familymenu_restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.petprojec.familymenu_restapi.repositories")
public class FamilymenuRestapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FamilymenuRestapiApplication.class, args);
	}

}
