package com.Anagrafe.entities.events;

import java.time.LocalDateTime;
import com.Anagrafe.entities.enums.EventType;
import com.Anagrafe.entities.BaseUser;

class AccountCreationEvent extends BaseEvent {

  private BaseUser user;

  public AccountCreationEvent(String eventId, BaseUser user) {
    super();
    this.eventId = eventId;
    this.user = user;
    this.eventType = EventType.ACCOUNT_CREATION;
    this.timestamp = LocalDateTime.now();
  }

  public BaseUser getUser() {
    return user;
  }

  public void setUser(BaseUser user) {
    this.user = user;
  }

}
