package com.example.DCP.Inventory.System.Repository;

import com.example.DCP.Inventory.System.Entity.BatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchRepository extends JpaRepository<BatchEntity, Long> {
}
