package org.petprojec.familymenu_restapi;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.petprojec.familymenu_restapi.controllers.DishesController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase
class FamilymenuRestapiApplicationTests {

	//@Autowired
	//private DishesRepository dishesRepository;

	@Autowired
	private DishesController dishesController;

	@Test
	void contextLoads() throws Exception {
		assertThat(dishesController).isNotNull();
	}

}
