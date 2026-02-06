package com.tutorial.tutorial.User.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    
    // Search by keyword (username or email)
    @Query("SELECT u FROM UserEntity u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))")
    Page<UserEntity> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
        @Param("username") String username, 
        @Param("email") String email, 
        Pageable pageable);
    
    // Search by keyword and role
    @Query("SELECT u FROM UserEntity u WHERE (LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND u.role = :role")
    Page<UserEntity> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseAndRole(
        @Param("username") String username, 
        @Param("email") String email, 
        @Param("role") UserRole role, 
        Pageable pageable);
}
