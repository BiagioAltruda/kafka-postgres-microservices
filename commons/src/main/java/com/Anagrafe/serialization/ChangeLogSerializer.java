package com.Anagrafe.serialization;

import org.apache.kafka.common.serialization.Serializer;

import com.Anagrafe.entities.ChangeLog;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

public class ChangeLogSerializer implements Serializer<ChangeLog> {
  /*
   * @Override
   * public void serialize(ChangeLog log, JsonGenerator jsonGenerator,
   * SerializerProvider serializerProvider)
   * throws IOException {
   * jsonGenerator.writeStartObject();
   * jsonGenerator.writeNumberField("id", log.getId());
   * jsonGenerator.writeStringField("generatorEvent",
   * log.getGeneratorEvent().toString());
   * jsonGenerator.writeObjectField("modifiedEntity", log.getModifiedEntity());
   * jsonGenerator.writeStringField("message", log.getMessage());
   * jsonGenerator.writeStringField("timestamp", log.getTimestamp().toString());
   * jsonGenerator.writeEndObject();
   * }
   */

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public byte[] serialize(String topic, ChangeLog log) {

    // allow jackson to use optionals
    objectMapper.registerModule(new Jdk8Module());
    objectMapper.registerModule(new JavaTimeModule());
    // include class info for polymorphic types so consumers can reconstruct concrete classes
    objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);

    if (log == null) {
      return null;
    }
    try {
      return objectMapper.writeValueAsBytes(log);
    } catch (Exception e) {
      throw new RuntimeException("Error while serializing log into JSON", e);
    }

  }

}
