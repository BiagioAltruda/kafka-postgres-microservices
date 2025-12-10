package com.Anagrafe.DocumentService;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;

import com.Anagrafe.entities.DocumentationRequest;
import com.Anagrafe.serialization.deserialization.DocumentationRequestDeserializer;

@Configuration
public class KafkaConsumerConfig {

  @Bean
  ConsumerFactory<String, DocumentationRequest> consumerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put("bootstrap.servers", "localhost:9092");
    configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "document-service");
    // Use ErrorHandlingDeserializer so DefaultErrorHandler can handle
    // SerializationException
    configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
    configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
    // delegate deserializers
    configProps.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
    configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, DocumentationRequestDeserializer.class);
    return new DefaultKafkaConsumerFactory<>(configProps);
  }

  @Bean
  ConcurrentKafkaListenerContainerFactory<String, DocumentationRequest> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, DocumentationRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    return factory;
  }
}
