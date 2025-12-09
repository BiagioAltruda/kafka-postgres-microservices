package com.Anagrafe.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;

import com.Anagrafe.entities.enums.EventType;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "logs")
public class ChangeLog implements Serializable {

  @JsonProperty("id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @JsonProperty("generatorEvent")
  @Transient
  private EventType generatorEvent;

  @Column(name = "generator_event")
  private String generatorEventString;

  // @Transient
  // private Optional<Loggable> modifiedEntity;
  // @Column(name = "modified_entity_id", nullable = true)
  @Transient
  private Optional<Persistable> modifiedEntity;
  @Column(name = "modified_entity_id", nullable = true)
  private Long modifiedEntityId;
  @JsonProperty("message")
  private String message;
  @JsonProperty("timestamp")
  private LocalDateTime timestamp;

  public ChangeLog(
      // EventType generatorEvent, Optional<Loggable> modifiedEntity,
      EventType generatorEvent, Optional<Persistable> modifiedEntity,
      String message, LocalDateTime timestamp) {

    this.generatorEventString = generatorEvent.toString();
    this.message = message;
    this.timestamp = timestamp;
    // this.modifiedEntity = modifiedEntity;
    if (modifiedEntity.isPresent()) {
      this.modifiedEntityId = extractId(modifiedEntity).orElse(Long.valueOf(0));
    }
    System.out.println("AFTER CONSTRUCTOR");
    System.out.println(this.toString());
  }

  // private Optional<Long> extractId(Optional<Loggable> modifiedEntity) {
  private Optional<Long> extractId(Optional<Persistable> modifiedEntity) {
    // Casting into BaseUser is safe because we know that the entity is a BaseUser
    // this way we can use it's methods and fields
    if (modifiedEntity.isEmpty()) {
      System.out.println("Entity missing");
      return Optional.empty();
    }
    if (modifiedEntity.get() instanceof BaseUser) {
      BaseUser tmpEntity;
      tmpEntity = (BaseUser) modifiedEntity.get();
      System.out.println("FROM CONSTRUCTOR");
      System.out.println(tmpEntity.toString());

      return Optional.ofNullable(tmpEntity.getId());
    } else {
      return Optional.empty();
    }

  }

  @Override
  public String toString() {

    return "id = " + getId() + " event = " + getGeneratorEvent() + " entity = "
        + getModifiedEntity() + " message = "
        + getMessage() + " timestamp = " + getTimestamp();
  }
}
