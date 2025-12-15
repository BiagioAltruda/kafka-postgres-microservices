
package com.Anagrafe.DocumentService;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.Anagrafe.DocumentService.service.DocumentationService;
import com.Anagrafe.entities.DocumentationRequest;

@Component
public class KafkaConsumer {

  private final DocumentationService documentationService;

  public KafkaConsumer(DocumentationService documentationService) {
    this.documentationService = documentationService;
  }

  @KafkaListener(topics = "documentation-upload", groupId = "accounts")
  public void listen(DocumentationRequest documents) {

    documentationService.saveBatch(documents);

  }
}
