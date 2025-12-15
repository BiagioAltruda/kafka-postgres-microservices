package com.Anagrafe.DocumentService.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

  @PostMapping("/upload")
  @Transactional
  public ResponseEntity<String> uploadDocument(@RequestBody DocumentationRequest request) {
    for (Document doc : request.getDocuments()) {
      Document newDoc = new Document();
      newDoc.setOwner(request.getUser());
      System.out.println("**********************************************************************");
      System.out.println("User:" + request.getUser().getChangeLog());
      System.out.println("**********************************************************************");
      newDoc.setDocumentType(doc.getDocumentType());
      newDoc.setCreationDate(doc.getCreationDate());
      newDoc.setExpirationDate(doc.getExpirationDate());

      documentationService.save(newDoc);
    }
    return ResponseEntity.ok("File uploaded successfully");
  }

}
