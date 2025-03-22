package com.example.DCP.Inventory.System.Repository;

import com.example.DCP.Inventory.System.Entity.CoordinatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoordinatorRepository extends JpaRepository<CoordinatorEntity, Long> {
    List<CoordinatorEntity> findBySchoolContact_School_SchoolRecordId(Long schoolRecordId);
}
