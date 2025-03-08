package com.example.DCP.Inventory.System.Repository;

import com.example.DCP.Inventory.System.Entity.CoordinatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoordinatorRepository extends JpaRepository<CoordinatorEntity, Long> {
}
