package com.Anagrafe.LogService.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

import com.Anagrafe.entities.ChangeLog;
import com.Anagrafe.entities.Loggable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "logs")
public class PersistableLog {
  // persistable wrapper for ChangeLog POJO

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "generator_event")
  private String generatorEvent;
  @Column(name = "modified_entity_id")
  private Long modifiedEntityId;
  private String message;
  private LocalDateTime timestamp;

  public PersistableLog(ChangeLog log) {
    this.id = log.getId();
    this.generatorEvent = log.getGeneratorEvent().getEventType();
    this.modifiedEntityId = Long.parseLong("1");
    this.message = log.getMessage();
    this.timestamp = log.getTimestamp();
  }

  @Deprecated
  /*
   * TODO: refactor to avoid reflection
   */
  private Long extractBaseObjectId(ChangeLog log) {

    Object loggable = log.getModifiedEntity().get();

    try {
      Method getId = loggable.getClass().getMethod("getId");
      return (Long) getId.invoke(loggable);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }
}
