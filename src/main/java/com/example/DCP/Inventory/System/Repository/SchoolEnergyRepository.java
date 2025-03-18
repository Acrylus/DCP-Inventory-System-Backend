package com.example.DCP.Inventory.System.Repository;

import com.example.DCP.Inventory.System.Entity.SchoolEnergyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SchoolEnergyRepository extends JpaRepository<SchoolEnergyEntity, Long> {
    List<SchoolEnergyEntity> findByEnergized(String energized);
    List<SchoolEnergyEntity> findByLocalGridSupply(String localGridSupply);
}
