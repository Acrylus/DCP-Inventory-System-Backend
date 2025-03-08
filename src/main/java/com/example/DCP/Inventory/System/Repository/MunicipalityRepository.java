package com.example.DCP.Inventory.System.Repository;

import com.example.DCP.Inventory.System.Entity.MunicipalityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MunicipalityRepository extends JpaRepository<MunicipalityEntity, Long> {
}
