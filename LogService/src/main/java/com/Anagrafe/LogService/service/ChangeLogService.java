package com.Anagrafe.LogService.service;

import org.springframework.stereotype.Service;

import com.Anagrafe.LogService.model.PersistableLog;
import com.Anagrafe.LogService.repository.ChangeLogRepo;

@Service
public class ChangeLogService {

  private final ChangeLogRepo changeLogRepo;

  public ChangeLogService(ChangeLogRepo changeLogRepo) {
    this.changeLogRepo = changeLogRepo;
  }

  public void save(PersistableLog log) {
    changeLogRepo.save(log);
  }

}
