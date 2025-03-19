package com.example.DCP.Inventory.System.Repository;

import com.example.DCP.Inventory.System.Entity.SchoolEntity;
import com.example.DCP.Inventory.System.Entity.SchoolNTCEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolNTCRepository extends JpaRepository<SchoolNTCEntity, Long> {
    Optional<SchoolNTCEntity> findBySchool(SchoolEntity school);
}
