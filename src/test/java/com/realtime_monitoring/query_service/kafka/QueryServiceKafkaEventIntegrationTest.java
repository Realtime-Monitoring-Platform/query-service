package com.realtime_monitoring.query_service.kafka;

import com.realtime_monitoring.query_service.event.DomainEvent;
import com.realtime_monitoring.query_service.event.TenantCreatedEvent;
import com.realtime_monitoring.query_service.kafkaa.TenantConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
	properties = {
		"eureka.client.enabled=false",
		"spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
		"spring.kafka.consumer.auto-offset-reset=earliest"
	}
)
@EmbeddedKafka(partitions = 1, topics = {"tenant-events-v4"})
@ActiveProfiles("test")
class QueryServiceKafkaEventIntegrationTest {

	@Autowired
	private EmbeddedKafkaBroker embeddedKafka;

	@Autowired
	private TenantConsumer tenantConsumer;

	@Test
	void queryServiceConsumerReceivesTenantCreatedEvent() throws Exception {
		Thread.sleep(2000);
		CountDownLatch latch = new CountDownLatch(1);
		tenantConsumer.setLatch(latch);

		Map<String, Object> producerProps = new HashMap<>();
		producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafka.getBrokersAsString());
		producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);
		producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.springframework.kafka.support.serializer.JsonSerializer.class);
		producerProps.put(org.springframework.kafka.support.serializer.JsonSerializer.TYPE_MAPPINGS,
				"tenantCreated:com.realtime_monitoring.query_service.event.TenantCreatedEvent," +
						"tenantUpdated:com.realtime_monitoring.query_service.event.TenantUpdatedEvent");

		var producerFactory = new org.springframework.kafka.core.DefaultKafkaProducerFactory<String, Object>(producerProps);
		var template = new org.springframework.kafka.core.KafkaTemplate<>(producerFactory);

		DomainEvent domainEvent = new DomainEvent(UUID.randomUUID(), "TENANT_CREATED", UUID.randomUUID(), "TENANT", Instant.now());
		TenantCreatedEvent event = new TenantCreatedEvent(domainEvent, "acme", "acme corp", "acme@example.com", "123", "ACTIVE");

		template.send("tenant-events-v4", event.event().eventId().toString(), event);
		template.flush();

		assertThat(latch.await(30, TimeUnit.SECONDS)).isTrue();
		assertThat(tenantConsumer.getLastEvent()).isInstanceOf(TenantCreatedEvent.class);
		assertThat(((TenantCreatedEvent) tenantConsumer.getLastEvent()).companyName()).isEqualTo("acme corp");
	}

	@AfterEach
	void tearDown() {
		tenantConsumer.setLatch(null);
		tenantConsumer.setLastEvent(null);
	}
}
