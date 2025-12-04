package com.Anagrafe.AdminService.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Anagrafe.AdminService.model.AdminUser;

@Repository
public interface UserRepository extends JpaRepository<AdminUser, Long> {

  Optional<AdminUser> findByUsername(String username);

}
