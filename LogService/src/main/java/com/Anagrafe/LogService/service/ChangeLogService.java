package com.Anagrafe.LogService.service;

import org.springframework.stereotype.Service;

import com.Anagrafe.LogService.repository.ChangeLogRepo;
import com.Anagrafe.entities.ChangeLog;

@Service
public class ChangeLogService {

  private final ChangeLogRepo changeLogRepo;

  public ChangeLogService(ChangeLogRepo changeLogRepo) {
    this.changeLogRepo = changeLogRepo;
  }

  public void save(ChangeLog log) {
    changeLogRepo.save(log);
  }

  public ChangeLog findById(Long id) {
    return changeLogRepo.findById(id).orElse(null);
  }

}
