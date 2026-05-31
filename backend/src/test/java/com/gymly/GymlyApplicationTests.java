package com.gymly;

import com.gymly.repository.ClassBookingRepository;
import com.gymly.repository.GymClassRepository;
import com.gymly.repository.MembershipPlanRepository;
import com.gymly.repository.MembershipRepository;
import com.gymly.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class GymlyApplicationTests {

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private MembershipPlanRepository membershipPlanRepository;

	@MockBean
	private MembershipRepository membershipRepository;

	@MockBean
	private GymClassRepository gymClassRepository;

	@MockBean
	private ClassBookingRepository classBookingRepository;

	@Test
	void contextLoads() {
	}

}
