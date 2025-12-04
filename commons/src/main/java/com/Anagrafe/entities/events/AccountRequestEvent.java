package com.Anagrafe.entities.events;

import java.time.LocalDateTime;
import com.Anagrafe.entities.enums.EventType;
import com.Anagrafe.entities.BaseUser;

class AccountRequestEvent extends BaseEvent {

  private BaseUser user;

  public AccountRequestEvent(String eventId, BaseUser user) {
    super();
    this.eventId = eventId;
    this.user = user;
    this.eventType = EventType.ACCOUNT_REQUEST;
    this.timestamp = LocalDateTime.now();
  }

  public BaseUser getUser() {
    return user;
  }

  public void setUser(BaseUser user) {
    this.user = user;
  }

}
