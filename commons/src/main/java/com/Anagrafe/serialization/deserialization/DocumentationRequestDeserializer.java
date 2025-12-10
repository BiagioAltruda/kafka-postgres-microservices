
package com.Anagrafe.serialization.deserialization;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import com.Anagrafe.entities.DocumentationRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class DocumentationRequestDeserializer implements Deserializer<DocumentationRequest> {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public DocumentationRequest deserialize(String topic, byte[] data) {

    objectMapper.registerModule(new JavaTimeModule());
    // accept embedded type information so polymorphic modifiedEntity can be
    // constructed
    objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
    // ignore extra properties (like derived getter-based properties) that consumers
    // don't need to set
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    if (data == null) {
      return null;
    }
    try {
      return objectMapper.readValue(data, DocumentationRequest.class);
    } catch (Exception e) {
      throw new SerializationException(e);
    }
  }

  // used to free resources after deserializer is shut down
  @Override
  public void close() {
  }

}
