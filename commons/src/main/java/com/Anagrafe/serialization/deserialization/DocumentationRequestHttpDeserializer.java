package com.Anagrafe.serialization.deserialization;

import java.io.IOException;

import org.apache.kafka.common.errors.SerializationException;

import com.Anagrafe.entities.DocumentationRequest;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class DocumentationRequestHttpDeserializer extends JsonDeserializer<DocumentationRequest> {

  @Override
  public DocumentationRequest deserialize(JsonParser parser, DeserializationContext context)
      throws IOException, JacksonException {
    JsonNode node = parser.getCodec().readTree(parser);

    // check if the node is an array or an object
    if (node.isArray()) {

      /*
       * TODO: implement deserialization for array of requests
       */

    }

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    try {
      return mapper.readValue(node.toString(), DocumentationRequest.class);
    } catch (Exception e) {
      throw new SerializationException(e);
    }
  }

}
