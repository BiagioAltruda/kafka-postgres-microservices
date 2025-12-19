package com.Anagrafe.DocumentService.controller;

import java.util.ArrayList;
import java.util.List;

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

  public DocumentationController(DocumentationService documentationService) {
    this.documentationService = documentationService;
  }

  @GetMapping
  public ResponseEntity<List<Document>> getDocuments(@RequestParam Long userId) {
    List<Document> docs = documentationService.findByUserId(userId).get();

    if (docs.isEmpty()) {
      return ResponseEntity.status(404).body(null);
    }
    return ResponseEntity.ok(docs);
  }

  @PostMapping("/upload")
  @Transactional
  public ResponseEntity<String> uploadDocument(@RequestBody DocumentationRequest request) {
    for (Document doc : request.getDocuments()) {
      Document newDoc = new Document();
      newDoc.setUserId(request.getUserId());
      System.out.println("**********************************************************************");
      System.out.println("User:" + request.getUserId());
      System.out.println("Date:" + doc.getCreationDate());
      System.out.println("Type:" + doc.getDocumentType());
      System.out.println("Expiration:" + doc.getExpirationDate());
      System.out.println("**********************************************************************");
      newDoc.setDocumentType(doc.getDocumentType());
      newDoc.setCreationDate(doc.getCreationDate());
      newDoc.setExpirationDate(doc.getExpirationDate());

      documentationService.save(newDoc);
    }
    return ResponseEntity.ok("File uploaded successfully");
  }

}
