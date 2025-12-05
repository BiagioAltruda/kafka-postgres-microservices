package com.Anagrafe.AdminService.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.Anagrafe.entities.ChangeLog;
import com.Anagrafe.entities.enums.EventType;

@Service
public class ChangeLogProducer {

  @Autowired
  private KafkaTemplate<String, ChangeLog> kafkaTemplate;

  public void send(ChangeLog changeLog) {
    for (EventType eventType : EventType.values()) {
      if (eventType.equals(changeLog.getGeneratorEvent())) {
        kafkaTemplate.send(eventType.getEventType(), changeLog);
        break;
      }
    }
  }
}
