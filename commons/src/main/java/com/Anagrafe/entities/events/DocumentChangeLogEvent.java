package com.Anagrafe.entities.events;

import java.time.LocalDateTime;

import com.Anagrafe.entities.ChangeLog;
import com.Anagrafe.entities.enums.EventType;

class DocumentChangeLogEvent extends BaseEvent {

  private ChangeLog changeLog;

  public DocumentChangeLogEvent(String eventId, ChangeLog changeLog) {
    super();
    this.eventId = eventId;
    this.changeLog = changeLog;
    this.eventType = EventType.DOCUMENT_CHANGE_LOG;
    this.timestamp = LocalDateTime.now();
  }

  public ChangeLog getChangeLog() {
    return changeLog;
  }

  public void setChangeLog(ChangeLog changeLog) {
    this.changeLog = changeLog;
  }

}
