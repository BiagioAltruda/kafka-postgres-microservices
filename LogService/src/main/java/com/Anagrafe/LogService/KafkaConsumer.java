package com.Anagrafe.LogService;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.Anagrafe.LogService.service.ChangeLogService;
import com.Anagrafe.entities.ChangeLog;
import com.Anagrafe.entities.Loggable;

@Component
public class KafkaConsumer {

  private final ChangeLogService changeLogService;

  public KafkaConsumer(ChangeLogService changeLogService) {
    this.changeLogService = changeLogService;
  }

  @KafkaListener(topics = "account-creation", groupId = "accounts")
  public void listen(ChangeLog log) {
    Loggable entity = log.getModifiedEntity().get();
    System.out.println(entity.getChangeLog());
    System.out.println(log.toString());

    changeLogService.save(log);

  }
}
