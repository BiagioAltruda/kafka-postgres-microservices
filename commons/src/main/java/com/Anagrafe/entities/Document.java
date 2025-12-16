package com.Anagrafe.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "documents")
public class Document extends Persistable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "user_id")
  private Long userId;
  @Column(name = "document_type")
  private String documentType;
  @Column(name = "creation_date")
  private LocalDateTime creationDate;
  @Column(name = "expiration_date")
  private LocalDateTime expirationDate;
  @Lob
  private byte[] data;

  public Document(Long userId, String documentType, LocalDateTime creationDate, LocalDateTime expirationDate) {
    this.userId = userId;
    this.documentType = documentType;
    this.creationDate = creationDate;
    this.expirationDate = expirationDate;
  }

}
