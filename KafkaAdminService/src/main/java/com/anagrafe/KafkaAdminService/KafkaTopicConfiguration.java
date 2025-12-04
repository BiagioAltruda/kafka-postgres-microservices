package com.anagrafe.KafkaAdminService;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfiguration {

  @Value(value = "${spring.kafka.bootstrap-servers}")
  private String bootstrapAddress;

  @Bean
  public KafkaAdmin kafkaAdmin() {
    Map<String, Object> configs = new HashMap<>();
    configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    return new KafkaAdmin(configs);
  }

  @Bean
  public NewTopic documentRequest() {
    // NewTopic("topicName, number of partitions, replication factor)
    return new NewTopic("document-request", 1, (short) 1);
  }

  @Bean
  public NewTopic acccountRequest() {
    // NewTopic("topicName, number of partitions, replication factor)
    return new NewTopic("accounts", 1, (short) 1);
  }
}
