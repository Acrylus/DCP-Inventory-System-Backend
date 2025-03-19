package com.example.DCP.Inventory.System.Repository;

import com.example.DCP.Inventory.System.Entity.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<PackageEntity, Long> {
    List<PackageEntity> findBySchoolBatchList_SchoolBatchId(Long schoolBatchId);
    List<PackageEntity> findByConfiguration_ConfigurationId(Long configurationId);
}
