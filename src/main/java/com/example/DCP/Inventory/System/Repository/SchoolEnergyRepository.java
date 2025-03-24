package com.example.DCP.Inventory.System.Repository;

import com.example.DCP.Inventory.System.Entity.SchoolContactEntity;
import com.example.DCP.Inventory.System.Entity.SchoolEnergyEntity;
import com.example.DCP.Inventory.System.Entity.SchoolEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolEnergyRepository extends JpaRepository<SchoolEnergyEntity, Long> {
    Optional<SchoolEnergyEntity> findBySchool(SchoolEntity school);
    SchoolEnergyEntity findBySchool_SchoolRecordId(Long schoolRecordId);
    @Modifying
    @Transactional
    @Query("DELETE FROM SchoolEnergyEntity e WHERE e.school.schoolRecordId = :schoolId")
    void deleteBySchoolId(@Param("schoolId") Long schoolId);

    @Query("SELECT sc FROM SchoolEnergyEntity sc JOIN FETCH sc.school")
    List<SchoolEnergyEntity> getAllWithSchool();

}
