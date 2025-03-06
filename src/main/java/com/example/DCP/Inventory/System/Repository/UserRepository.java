package com.example.DCP.Inventory.System.Repository;

import com.example.DCP.Inventory.System.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    UserEntity findOneByUsername(String username);
    boolean existsByUsername(String username);
}
