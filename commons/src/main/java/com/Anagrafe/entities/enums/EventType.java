package com.Anagrafe.entities.enums;

public enum EventType {
  DOCUMENT_CREATION("document-creation"),
  DOCUMENT_REQUEST("document-request"),
  DOCUMENT_UPDATE("document-update"),
  DOCUMENT_CHANGE_LOG("document-change-log"),
  ACCOUNT_CREATION("account-creation"),
  ACCOUNT_REQUEST("account-request"),
  ACCOUNT_UPDATE("account-update");

  private final String eventType;

  private EventType(String eventType) {
    this.eventType = eventType;
  }

  public String getEventType() {
    return eventType;
  }
}
