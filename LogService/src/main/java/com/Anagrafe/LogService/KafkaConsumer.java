package com.Anagrafe.LogService;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.Anagrafe.LogService.service.ChangeLogService;
import com.Anagrafe.entities.ChangeLog;

@Component
public class KafkaConsumer {

  private final ChangeLogService changeLogService;

  public KafkaConsumer(ChangeLogService changeLogService) {
    this.changeLogService = changeLogService;
  }

  @KafkaListener(topics = "account-creation", groupId = "accounts")
  public void listen(ChangeLog log) {

    changeLogService.save(log);

  }
}
