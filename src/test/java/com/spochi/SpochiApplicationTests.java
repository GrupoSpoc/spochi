package com.spochi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("disable-firebase")
class SpochiApplicationTests {

	@Test
	void contextLoads() {
	}

}
