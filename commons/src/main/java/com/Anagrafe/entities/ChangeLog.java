package com.Anagrafe.entities;

import java.time.LocalDateTime;
import java.util.Optional;

import com.Anagrafe.entities.enums.EventType;

public class ChangeLog {

  private Long id;
  private EventType generatorEvent;
  private Optional<Object> modifiedEntity;
  private String message;
  private LocalDateTime timestamp;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public EventType getGeneratorEvent() {
    return generatorEvent;
  }

  public void setGeneratorEvent(EventType generatorEvent) {
    this.generatorEvent = generatorEvent;
  }

  public Optional<Object> getModifiedEntity() {
    return modifiedEntity;
  }

  public void setModifiedEntity(Optional<Object> modifiedEntity) {
    this.modifiedEntity = modifiedEntity;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

}
