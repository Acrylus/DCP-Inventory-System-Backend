package com.example.DCP.Inventory.System.Repository;

import com.example.DCP.Inventory.System.Entity.SchoolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<SchoolEntity, Long> {
    boolean existsByName(String name);

    @Query("SELECT s.schoolRecordId FROM SchoolEntity s WHERE s.schoolId = :schoolId")
    Optional<Long> findSchoolRecordIdBySchoolId(@Param("schoolId") String schoolId);

    @Query("SELECT s.id FROM SchoolEntity s WHERE s.name = :schoolName")
    Optional<Long> findSchoolRecordIdByName(@Param("schoolName") String schoolName);

    long countBySchoolId(String schoolId);
}
