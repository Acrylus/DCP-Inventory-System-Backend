package com.example.DCP.Inventory.System.Repository;

import com.example.DCP.Inventory.System.Entity.BatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchRepository extends JpaRepository<BatchEntity, Long> {
}
