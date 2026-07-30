package com.realtime_monitoring.query_service.kafka;

import com.realtime_monitoring.query_service.event.TenantCreatedEvent;
import com.realtime_monitoring.query_service.event.TenantUpdatedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id:query-service-group}")
    private String groupId;

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);

        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.realtime_monitoring.query_service.event");
        props.put(
                JsonDeserializer.TRUSTED_PACKAGES,
                "*");
        props.put(JsonDeserializer.TYPE_MAPPINGS,
                        "tenantCreated:com.realtime_monitoring.query_service.event.TenantCreatedEvent," +
                        "tenantUpdated:com.realtime_monitoring.query_service.event.TenantUpdatedEvent," +
                        "tenantDeleted:com.realtime_monitoring.query_service.event.TenantDeletedEvent," +
                        "deviceCreated:com.realtime_monitoring.query_service.event.DeviceCreatedEvent," +
                        "deviceUpdated:com.realtime_monitoring.query_service.event.DeviceUpdatedEvent," +
                        "deviceDeleted:com.realtime_monitoring.query_service.event.DeviceDeletedEvent," +
                        "userCreated:com.realtime_monitoring.query_service.event.UserCreatedEvent," +
                        "userUpdated:com.realtime_monitoring.query_service.event.UserUpdatedEvent," +
                        "userDeleted:com.realtime_monitoring.query_service.event.UserDeletedEvent," +
                        "teamCreated:com.realtime_monitoring.query_service.event.TeamCreatedEvent," +
                        "teamUpdated:com.realtime_monitoring.query_service.event.TeamUpdatedEvent," +
                        "teamDeleted:com.realtime_monitoring.query_service.event.TeamDeletedEvent," +
                        "roleCreated:com.realtime_monitoring.query_service.event.RoleCreatedEvent," +
                        "roleUpdated:com.realtime_monitoring.query_service.event.RoleUpdatedEvent," +
                        "roleDeleted:com.realtime_monitoring.query_service.event.RoleDeletedEvent"

        );

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}