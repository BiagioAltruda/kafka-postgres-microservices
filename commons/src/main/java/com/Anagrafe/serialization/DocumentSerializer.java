
package com.Anagrafe.serialization;

import org.apache.kafka.common.serialization.Serializer;

import com.Anagrafe.entities.Document;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

public class DocumentSerializer implements Serializer<Document> {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public byte[] serialize(String topic, Document user) {

    objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);

    if (user == null) {
      return null;
    }
    try {
      return objectMapper.writeValueAsBytes(user);
    } catch (Exception e) {
      throw new RuntimeException("Error while serializing document into JSON", e);
    }

  }

}
