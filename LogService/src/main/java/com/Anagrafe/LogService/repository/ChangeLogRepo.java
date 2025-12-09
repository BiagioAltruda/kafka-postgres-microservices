package com.Anagrafe.LogService.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Anagrafe.entities.ChangeLog;

@Repository
public interface ChangeLogRepo extends JpaRepository<ChangeLog, Long> {

  Optional<ChangeLog> findById(Long id);

}
