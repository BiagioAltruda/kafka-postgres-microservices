package com.Anagrafe.AdminService.configs;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.Anagrafe.entities.BaseUser;
import com.Anagrafe.entities.ChangeLog;
import com.Anagrafe.entities.Document;
import com.Anagrafe.entities.DocumentationRequest;
import com.Anagrafe.serialization.BaseUserSerializer;
import com.Anagrafe.serialization.ChangeLogSerializer;
import com.Anagrafe.serialization.DocumentSerializer;
import com.Anagrafe.serialization.DocumentationRequestSerializer;

@Configuration
public class KafkaProducerConfig {
  @Bean
  ProducerFactory<String, String> producerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put("bootstrap.servers", "localhost:9092");
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ChangeLogSerializer.class);
    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  ProducerFactory<String, ChangeLog> logProducerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put("bootstrap.servers", "localhost:9092");
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ChangeLogSerializer.class);
    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  ProducerFactory<String, BaseUser> userProducerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put("bootstrap.servers", "localhost:9092");
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, BaseUserSerializer.class);
    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  ProducerFactory<String, Document> documentProducerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put("bootstrap.servers", "localhost:9092");
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, DocumentSerializer.class);
    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  ProducerFactory<String, DocumentationRequest> documentationRequestProducerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put("bootstrap.servers", "localhost:9092");
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, DocumentationRequestSerializer.class);
    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  public KafkaTemplate<String, String> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }

  @Bean
  public KafkaTemplate<String, ChangeLog> kafkaLogTemplate(ProducerFactory<String, ChangeLog> logProducerFactory) {
    return new KafkaTemplate<>(logProducerFactory);
  }

  @Bean
  public KafkaTemplate<String, BaseUser> kafkaUserTemplate(ProducerFactory<String, BaseUser> userProducerFactory) {
    return new KafkaTemplate<>(userProducerFactory);
  }

  @Bean
  public KafkaTemplate<String, Document> kafkaDocumentTemplate(
      ProducerFactory<String, Document> documentProducerFactory) {
    return new KafkaTemplate<>(documentProducerFactory);
  }

  @Bean
  public KafkaTemplate<String, DocumentationRequest> kafkaDocumentationRequestTemplate(
      ProducerFactory<String, DocumentationRequest> documentationRequestProducerFactory) {
    return new KafkaTemplate<>(documentationRequestProducerFactory);
  }
}
