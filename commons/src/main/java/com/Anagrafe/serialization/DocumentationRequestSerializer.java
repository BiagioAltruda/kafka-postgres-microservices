package com.Anagrafe.serialization;

import org.apache.kafka.common.serialization.Serializer;

import com.Anagrafe.entities.DocumentationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class DocumentationRequestSerializer implements Serializer<DocumentationRequest> {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public byte[] serialize(String topic, DocumentationRequest request) {

    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);

    if (request == null) {
      return null;
    }
    try {
      return objectMapper.writeValueAsBytes(request);
    } catch (Exception e) {
      throw new RuntimeException("Error while serializing document into JSON", e);
    }

  }

}
