package com.Anagrafe.AdminService.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.Anagrafe.entities.Document;
import com.Anagrafe.entities.enums.DocumentOperation;

@Service
public class DocumentProducer {

  @Autowired
  private KafkaTemplate<String, Document> kafkaTemplate;

  public void send(Document document, DocumentOperation operation) {
    kafkaTemplate.send(operation.toString(), document);
  }
}
