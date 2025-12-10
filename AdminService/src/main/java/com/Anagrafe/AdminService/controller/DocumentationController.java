package com.Anagrafe.AdminService.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Anagrafe.entities.BaseUser;
import com.Anagrafe.entities.Document;
import com.Anagrafe.entities.DocumentationRequest;

@RestController
@RequestMapping("/documentation")
public class DocumentationController {

  private final KafkaTemplate<String, DocumentationRequest> kafkaDocumentationRequestTemplate;

  public DocumentationController(KafkaTemplate<String, DocumentationRequest> kafkaDocumentationRequestTemplate) {
    this.kafkaDocumentationRequestTemplate = kafkaDocumentationRequestTemplate;
  }

  @GetMapping("/get-docs")
  public ResponseEntity<List<Document>> getUserDocuments() {

    try {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      @SuppressWarnings("unused")
      BaseUser user = (BaseUser) auth.getPrincipal();

      /**
       * TODO: get document with kafka
       */
    } catch (Exception e) {
      throw new AccessDeniedException("User not logged in");
    }

    return null;
  }

  @PostMapping("/upload")
  public ResponseEntity<String> uploadDocument(@RequestBody DocumentationRequest request) {

    // max number of documents to be processes
    // synchronously
    final Integer syncrhronousLimit = 5;
    /**
     * TODO: check document size, if size is above some value,
     * defer the operation to another service with kafka
     */
    if (request.getSize() > syncrhronousLimit) {

      kafkaDocumentationRequestTemplate.send(request.getOperation().toString(), request);

    } else {

    }
    return null;
  }

  @SuppressWarnings("unused")
  private Document createDocument(BaseUser owner, String documentType, LocalDateTime creationDate,
      LocalDateTime expirationDate) {

    return null;
  }

}
