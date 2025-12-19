package com.Anagrafe.DocumentService.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Anagrafe.DocumentService.service.DocumentationService;
import com.Anagrafe.entities.Document;
import com.Anagrafe.entities.DocumentationRequest;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/documentation")
public class DocumentationController {

  private final DocumentationService documentationService;
  private final Logger logger = LogManager.getLogger("com.Anagrafe.docs");

  public DocumentationController(DocumentationService documentationService) {
    this.documentationService = documentationService;
  }

  @GetMapping
  public ResponseEntity<List<Document>> getDocuments(@RequestParam Long userId) {
    logger.info("Fetching documents for userId: " + userId);
    List<Document> docs = documentationService.findByUserId(userId).get();

    if (docs.isEmpty()) {
      logger.warn("No documents found for userId: " + userId);
      return ResponseEntity.status(404).body(null);
    }
    logger.info("Found " + docs.size() + " documents for userId: " + userId);
    return ResponseEntity.ok(docs);
  }

  @PostMapping("/upload")
  @Transactional
  public ResponseEntity<String> uploadDocument(@RequestBody DocumentationRequest request) {
    for (Document doc : request.getDocuments()) {
      Document newDoc = new Document();
      newDoc.setUserId(request.getUserId());

      logger.info("User:" + request.getUserId());
      logger.info("Date:" + doc.getCreationDate());
      logger.info("Expiration:" + doc.getExpirationDate());
      logger.info("Type:" + doc.getDocumentType());

      newDoc.setData(doc.getData());
      newDoc.setDocumentType(doc.getDocumentType());
      newDoc.setCreationDate(doc.getCreationDate());
      newDoc.setExpirationDate(doc.getExpirationDate());

      documentationService.save(newDoc);
    }
    return ResponseEntity.ok("File uploaded successfully");
  }

}
