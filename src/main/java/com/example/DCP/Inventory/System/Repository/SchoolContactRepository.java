package com.example.DCP.Inventory.System.Repository;

import com.example.DCP.Inventory.System.Entity.SchoolBatchListEntity;
import com.example.DCP.Inventory.System.Entity.SchoolContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolContactRepository extends JpaRepository<SchoolContactEntity, Long> {
    List<SchoolContactEntity> findBySchool_SchoolId(Long schoolId);
}
