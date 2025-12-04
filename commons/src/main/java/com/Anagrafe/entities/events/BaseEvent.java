package com.Anagrafe.entities.events;

import java.time.LocalDateTime;
import com.Anagrafe.entities.enums.EventType;

public abstract class BaseEvent {

  protected String eventId;
  protected EventType eventType;
  protected LocalDateTime timestamp;

}
