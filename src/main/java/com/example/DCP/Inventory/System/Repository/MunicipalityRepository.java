package com.example.DCP.Inventory.System.Repository;

import com.example.DCP.Inventory.System.Entity.ConfigurationEntity;
import com.example.DCP.Inventory.System.Entity.DivisionEntity;
import com.example.DCP.Inventory.System.Entity.MunicipalityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MunicipalityRepository extends JpaRepository<MunicipalityEntity, Long> {
    List<MunicipalityEntity> findByDivision_DivisionId(Long divisionId);
}
