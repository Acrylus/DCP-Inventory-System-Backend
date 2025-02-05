package com.example.DCP.Inventory.System.Repository;

import com.example.DCP.Inventory.System.Entity.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageRepository extends JpaRepository<PackageEntity, Long> {
}
