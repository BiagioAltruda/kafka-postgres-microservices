package com.Anagrafe.entities;

import java.time.LocalDateTime;
import java.util.Optional;

import com.Anagrafe.entities.enums.EventType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.Anagrafe.serialization.ChangeLogSerializer;

public class ChangeLog {

  @JsonProperty("id")
  private Long id;
  @JsonProperty("generatorEvent")
  private EventType generatorEvent;
  @JsonProperty("modifiedEntity")
  private Optional<Loggable> modifiedEntity;
  @JsonProperty("message")
  private String message;
  @JsonProperty("timestamp")
  private LocalDateTime timestamp;

  public ChangeLog() {
  }

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
