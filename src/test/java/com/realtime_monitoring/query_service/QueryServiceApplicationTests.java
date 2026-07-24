package com.realtime_monitoring.query_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"eureka.client.enabled=false", "protobuf.skip=true"})
class QueryServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
