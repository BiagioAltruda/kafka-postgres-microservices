package com.Anagrafe.LogService;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.Anagrafe.entities.ChangeLog;
import com.Anagrafe.entities.Loggable;

@Component
public class KafkaConsumer {

  @KafkaListener(topics = "account-creation", groupId = "accounts")
  public void listen(ChangeLog log) {
    Loggable entity = log.getModifiedEntity().get();
    System.out.println(entity.getChangeLog());
  }
}
