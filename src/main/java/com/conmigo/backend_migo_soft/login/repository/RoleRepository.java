package com.conmigo.backend_migo_soft.login.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.conmigo.backend_migo_soft.login.models.ERole;
import com.conmigo.backend_migo_soft.login.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
