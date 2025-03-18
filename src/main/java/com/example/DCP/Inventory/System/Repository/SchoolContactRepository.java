package com.example.DCP.Inventory.System.Repository;

import com.example.DCP.Inventory.System.Entity.SchoolContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolContactRepository extends JpaRepository<SchoolContactEntity, Long> {
}
