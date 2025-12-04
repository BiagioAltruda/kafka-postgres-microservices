package com.Anagrafe.entities;

import java.time.LocalDateTime;
import java.util.Optional;

import com.Anagrafe.entities.enums.EventType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.Anagrafe.serialization.ChangeLogSerializer;

@JsonSerialize(using = ChangeLogSerializer.class)
public class ChangeLog {

  private Long id;
  private EventType generatorEvent;
  private Optional<Loggable> modifiedEntity;
  private String message;
  private LocalDateTime timestamp;

  public ChangeLog(
      Long id, EventType generatorEvent, Optional<Loggable> modifiedEntity,
      String message, LocalDateTime timestamp) {
    this.id = id;
    this.generatorEvent = generatorEvent;
    this.message = message;
    this.timestamp = timestamp;
    this.modifiedEntity = modifiedEntity;
  }

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

  public Optional<Loggable> getModifiedEntity() {
    return modifiedEntity;
  }

  public void setModifiedEntity(Optional<Loggable> modifiedEntity) {
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
