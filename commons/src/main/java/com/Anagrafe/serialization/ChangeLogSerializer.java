package com.Anagrafe.serialization;

import java.io.IOException;

import com.Anagrafe.entities.ChangeLog;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ChangeLogSerializer extends JsonSerializer<ChangeLog> {

  @Override
  public void serialize(ChangeLog log, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
      throws IOException {
    jsonGenerator.writeStartObject();
    jsonGenerator.writeNumberField("id", log.getId());
    jsonGenerator.writeStringField("generatorEvent", log.getGeneratorEvent().toString());
    jsonGenerator.writeObjectField("modifiedEntity", log.getModifiedEntity());
    jsonGenerator.writeStringField("message", log.getMessage());
    jsonGenerator.writeStringField("timestamp", log.getTimestamp().toString());
    jsonGenerator.writeEndObject();
  }

}
