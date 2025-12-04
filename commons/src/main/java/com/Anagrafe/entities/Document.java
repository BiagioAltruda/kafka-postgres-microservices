package com.Anagrafe.entities;

import java.time.LocalDateTime;

public class Document<U extends BaseUser> implements Loggable {

  private U owner;
  private String documentType;
  private LocalDateTime creationDate;
  private LocalDateTime expirationDate;

  public Document(U owner, String documentType, LocalDateTime creationDate, LocalDateTime expirationDate) {
    this.owner = owner;
    this.documentType = documentType;
    this.creationDate = creationDate;
    this.expirationDate = expirationDate;
  }

  public U getOwner() {
    return owner;
  }

  public void setOwner(U owner) {
    this.owner = owner;
  }

  public String getDocumentType() {
    return documentType;
  }

  public void setDocumentType(String documentType) {
    this.documentType = documentType;
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDateTime creationDate) {
    this.creationDate = creationDate;
  }

  public LocalDateTime getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(LocalDateTime expirationDate) {
    this.expirationDate = expirationDate;
  }

  @Override
  public String getChangeLog() {
    return "Document owner: " + owner.getUsername() + ". Created at: " + creationDate + ". Expiration date: "
        + expirationDate;
  }

}
