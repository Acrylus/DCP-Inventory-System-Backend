package com.example.DCP.Inventory.System.Repository;

import com.example.DCP.Inventory.System.Entity.SchoolEnergyEntity;
import com.example.DCP.Inventory.System.Entity.SchoolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolEnergyRepository extends JpaRepository<SchoolEnergyEntity, Long> {
    Optional<SchoolEnergyEntity> findBySchool(SchoolEntity school);
}
