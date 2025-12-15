package com.Anagrafe.DocumentService.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.Anagrafe.DocumentService.repository.DocumentationRepository;
import com.Anagrafe.entities.Document;
import com.Anagrafe.entities.DocumentationRequest;

@Service
public class DocumentationService {

  private final DocumentationRepository documentationRepository;

  public DocumentationService(DocumentationRepository documentationRepository) {
    this.documentationRepository = documentationRepository;
  }

  public void save(Document doc) {
    documentationRepository.save(doc);
  }

  public Document findById(Long id) {
    return documentationRepository.findById(id).orElse(null);
  }

  public void saveBatch(DocumentationRequest request) {
    for (Document document : request.getDocuments()) {
      documentationRepository.save(document);
    }
  }

  public Optional<DocumentationRequest> findByOwner(String owner) {
    return documentationRepository.findByOwner(owner);
  }
}
