package com.sid.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sid.demo.models.User;
import com.sid.demo.models.UserRole;

@Repository
public interface UserRoleRepo extends JpaRepository<UserRole, Long>{

    Optional<UserRole> findByAuthority(String authority);
}
