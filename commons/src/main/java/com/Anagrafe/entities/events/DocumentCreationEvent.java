package com.Anagrafe.entities.events;

import java.time.LocalDateTime;

import com.Anagrafe.entities.Document;
import com.Anagrafe.entities.enums.EventType;

class DocumentCreationEvent extends BaseEvent {

  private Document<?> document;

  public DocumentCreationEvent(String eventId, Document<?> document) {
    super();
    this.eventId = eventId;
    this.document = document;
    this.eventType = EventType.DOCUMENT_CREATION;
    this.timestamp = LocalDateTime.now();
  }

  public Document<?> getDocument() {
    return document;
  }

  public void setDocument(Document<?> document) {
    this.document = document;
  }

}
