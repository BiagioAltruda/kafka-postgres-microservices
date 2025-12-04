package com.Anagrafe.LogService.configs;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import com.Anagrafe.LogService.utils.ChangeLogDeserializer;
import com.Anagrafe.entities.ChangeLog;

public class KafkaConsumerConfig {

  @Bean
  ConsumerFactory<String, ChangeLog> consumerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put("bootstrap.servers", "localhost:9092");
    configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "log-service");
    configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    // custom deserializer for changelog object
    configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ChangeLogDeserializer.class);
    return new DefaultKafkaConsumerFactory<>(configProps);
  }

  @Bean
  ConcurrentKafkaListenerContainerFactory<String, ChangeLog> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, ChangeLog> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    return factory;
  }
}
