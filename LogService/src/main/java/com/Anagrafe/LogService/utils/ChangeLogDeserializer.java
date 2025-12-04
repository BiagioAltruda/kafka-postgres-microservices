package com.Anagrafe.LogService.utils;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import com.Anagrafe.entities.ChangeLog;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ChangeLogDeserializer implements Deserializer<ChangeLog> {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public ChangeLog deserialize(String topic, byte[] data) {
    if (data == null) {
      return null;
    }
    try {
      // tries to deserialize the changelog object
      return objectMapper.readValue(data, ChangeLog.class);
    } catch (Exception e) {
      throw new SerializationException(e);
    }
  }

  // used to free resources after deserializer is shut down
  @Override
  public void close() {
  }

}
