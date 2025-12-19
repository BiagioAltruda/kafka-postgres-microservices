package com.Anagrafe.DocumentService.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Anagrafe.entities.Document;

@Repository
public interface DocumentationRepository extends JpaRepository<Document, Long> {

  Optional<Document> findById(Long id);

  Optional<List<Document>> findByUserId(Long userId);
}
