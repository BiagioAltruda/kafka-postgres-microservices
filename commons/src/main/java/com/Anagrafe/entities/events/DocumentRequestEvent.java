package com.Anagrafe.entities.events;

import java.time.LocalDateTime;
import com.Anagrafe.entities.Document;
import com.Anagrafe.entities.enums.EventType;

class DocumentRequestEvent extends BaseEvent {

  private Document<?> document;

  public DocumentRequestEvent(String eventId, Document<?> document) {
    super();
    this.eventId = eventId;
    this.document = document;
    this.eventType = EventType.DOCUMENT_REQUEST;
    this.timestamp = LocalDateTime.now();
  }

  public Document<?> getDocument() {
    return document;
  }

  public void setDocument(Document<?> document) {
    this.document = document;
  }

}
