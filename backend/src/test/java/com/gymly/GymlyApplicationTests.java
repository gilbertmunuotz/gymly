package com.gymly;

import com.gymly.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class GymlyApplicationTests {

	@MockBean
	private UserRepository userRepository;

	@Test
	void contextLoads() {
	}

}
