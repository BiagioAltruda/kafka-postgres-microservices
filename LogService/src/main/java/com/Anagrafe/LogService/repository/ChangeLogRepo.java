package com.Anagrafe.LogService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Anagrafe.LogService.model.PersistableLog;

@Repository
public interface ChangeLogRepo extends JpaRepository<PersistableLog, Long> {

}
