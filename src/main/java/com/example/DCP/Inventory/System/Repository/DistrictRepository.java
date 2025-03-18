package com.example.DCP.Inventory.System.Repository;

import com.example.DCP.Inventory.System.Entity.ConfigurationEntity;
import com.example.DCP.Inventory.System.Entity.DistrictEntity;
import com.example.DCP.Inventory.System.Entity.DivisionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<DistrictEntity, Long> {
    List<DistrictEntity> findByDivision_DivisionId(Long divisionId);
}
