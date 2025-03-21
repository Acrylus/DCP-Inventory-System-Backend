package com.example.DCP.Inventory.System.Repository;

import com.example.DCP.Inventory.System.Entity.SchoolContactEntity;
import com.example.DCP.Inventory.System.Entity.SchoolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import java.util.List;

@Repository
public interface SchoolContactRepository extends JpaRepository<SchoolContactEntity, Long> {
    Optional<SchoolContactEntity> findBySchool(SchoolEntity school);
    List<SchoolContactEntity> findBySchool_SchoolRecordId(Long schoolRecordId);
}
