package com.example.DCP.Inventory.System.Repository;

import com.example.DCP.Inventory.System.Entity.DistrictEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository extends JpaRepository<DistrictEntity, Long> {
}
