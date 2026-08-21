package com.realtime_monitoring.query_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {"eureka.client.enabled=false", "protobuf.skip=true"})
@ActiveProfiles("test")
class QueryServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
