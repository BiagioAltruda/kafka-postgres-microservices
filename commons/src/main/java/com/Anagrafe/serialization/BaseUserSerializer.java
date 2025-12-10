
package com.Anagrafe.serialization;

import org.apache.kafka.common.serialization.Serializer;

import com.Anagrafe.entities.BaseUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

public class BaseUserSerializer implements Serializer<BaseUser> {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public byte[] serialize(String topic, BaseUser user) {

    objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);

    if (user == null) {
      return null;
    }
    try {
      return objectMapper.writeValueAsBytes(user);
    } catch (Exception e) {
      throw new RuntimeException("Error while serializing user into JSON", e);
    }

  }

}
