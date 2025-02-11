package com.example.DCP.Inventory.System.Repository;

import com.example.DCP.Inventory.System.Entity.SchoolEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<SchoolEntity, Long> {
    boolean existsByName(String name);
}
