package com.tutorial.tutorial.User.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tutorial.tutorial.User.entity.UserEntity;
import com.tutorial.tutorial.User.entity.UserRole;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    List<UserEntity> findByActive(Boolean active);
    Page<UserEntity> findByRole(UserRole role, Pageable pageable);
}
