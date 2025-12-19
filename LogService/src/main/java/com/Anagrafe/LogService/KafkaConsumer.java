package com.Anagrafe.LogService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.Anagrafe.LogService.service.ChangeLogService;
import com.Anagrafe.entities.ChangeLog;

@Component
public class KafkaConsumer {

  private final ChangeLogService changeLogService;
  private static final Logger LOGGER = LogManager.getLogger();

  public KafkaConsumer(ChangeLogService changeLogService) {
    this.changeLogService = changeLogService;
  }

  @KafkaListener(topics = "account-creation", groupId = "accounts")
  public void listen(ChangeLog log) {

    LOGGER.info("Received ChangeLog: " + log);
    changeLogService.save(log);

  }

  @KafkaListener(topics = "account-deletion", groupId = "accounts")
  public void listenDeletion(String log) {

    System.out.println("testing");
    LOGGER.info("Received ChangeLog: {}", log);

  }
}
